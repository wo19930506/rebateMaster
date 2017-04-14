package com.edm.modules.orm.ibatis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.edm.modules.orm.Page;
import com.edm.modules.utils.Asserts;
import com.edm.modules.utils.Reflections;

/**
 * 封装iBatis原生API的泛型基类.
 * 
 * @author yjli
 */
public class SimpleSqlMapClientDao<T, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SqlMapClientTemplate template;

	protected Class<T> entityClass;

	/**
	 * 用于Dao层子类使用的构造函数. 
	 * 通过子类的泛型定义获取对象类型Class.
	 */
	public SimpleSqlMapClientDao() {
		this.entityClass = Reflections.getSuperClassGenericType(getClass());
	}

	/**
	 * 获取SqlMapClientTemplate.
	 */
	public SqlMapClientTemplate getTemplate() {
		return template;
	}

	/**
	 * 设置SqlMapClientTemplate.
	 */
	@Autowired
	public void setTemplate(final SqlMapClientTemplate template) {
		this.template = template;
	}

	// ** 泛型函数 **
	
	/**
	 * 保存对象.
	 */
	public Object save(final T entity) {
		Asserts.notNull(entity, "entity不能为空");
		return template.insert(getStatementName("save"), entity);
	}
	
	public Object save(final String statementName, final T entity) {
		Asserts.notNull(entity, "entity不能为空");
		return template.insert(statementName, entity);
	}

	/**
	 * 修改对象.
	 */
	public int update(final T entity) {
		Asserts.notNull(entity, "entity不能为空");
		return template.update(getStatementName("update"), entity);
	}

	public int update(final String statementName, final T entity) {
		Asserts.notNull(entity, "entity不能为空");
		return template.update(statementName, entity);
	}
	
	/**
	 * 删除对象.
	 */
	public int delete(final PK id) {
		Asserts.notNull(id, "id不能为空");
		return template.delete(getStatementName("delete"), id);
	}
	
	/**
	 * 删除对象.
	 */
	public int delete(final String statementName, final PK id) {
		Asserts.notNull(id, "id不能为空");
		return template.delete(statementName, id);
	}
	
	/**
	 * 删除对象.
	 */
	public int delete(final String statementName, final Object parameterObject) {
		Asserts.notNull(parameterObject, "parameterObject不能为空");
		return template.delete(statementName, parameterObject);
	}

	/**
	 * 查询对象.
	 */
	@SuppressWarnings("unchecked")
	public T get(final PK id) {
		Asserts.notNull(id, "id不能为空");
		return (T) template.queryForObject(getStatementName("get"), id);
	}

	/**
	 * 查询对象集合.
	 */
	@SuppressWarnings("unchecked")
	public List<T> find() {
		return template.queryForList(getStatementName("find"));
	}

	/**
	 * 分页查询对象集合.
	 */
	public Page<T> find(final Page<T> page, final Map<String, Object> map) {
		return find(page, map, getStatementName("count"), getStatementName("index"));
	}
	
	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 */
	public boolean isUnique(final Object newValue, final Object oldValue) {
		return isPropertyUnique(getStatementName("isUnique"), newValue, oldValue);
	}
	
	// ** 扩展函数 **
	
	/**
	 * 查询对象.
	 */
	@SuppressWarnings("unchecked")
	public <X> X get(final String statementName, final Object parameterObject) {
		Asserts.hasText(statementName, "statementName不能为空");
		Asserts.notNull(parameterObject, "parameterObject不能为空");
		return (X) template.queryForObject(statementName, parameterObject);
	}

	/**
	 * 查询对象集合.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> find(final String statementName) {
		Asserts.hasText(statementName, "statementName不能为空");
		return template.queryForList(statementName);
	}
	
	/**
	 * 查询对象集合.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> find(final String statementName, final Object parameterObject) {
		Asserts.hasText(statementName, "statementName不能为空");
		Asserts.notNull(parameterObject, "parameterObject不能为空");
		return template.queryForList(statementName, parameterObject);
	}

	/**
	 * 执行count查询获取总记录数.
	 */
	public long countResult(final String statementName, final Object parameterObject) {
		Asserts.hasText(statementName, "statementName不能为空");
		Asserts.notNull(parameterObject, "parameterObject不能为空");
		Long count = get(statementName, parameterObject);
		return (count == null ? 0 : count.longValue());
	}

	/**
	 * 分页查询对象集合.
	 */
	public <X> Page<X> find(final Page<X> page, final Map<String, Object> map, final String countStatementName, final String indexStatementName) {
		Asserts.notNull(page, "page不能为空");

		// 设置总记录数
		if (page.isAutoCount()) {
			long totalItems = countResult(countStatementName, map);
			page.setTotalItems(totalItems);
			
			// 高于totalItems时自动调整为totalPages.
			int totalPages = (int) page.getTotalPages();
			if (page.getPageNo() > totalPages) {
				page.setPageNo(totalPages);
			}
		}

		// 设置分页
		map.put("firstResult", page.getOffset());
		map.put("pageSize", page.getPageSize());

		// 设置排序
		map.put("orderBy", page.getOrderBy());
		map.put("order", page.getOrder());

		List<X> result = find(indexStatementName, map);
		page.setResult(result);

		return page;
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 */
	public boolean isPropertyUnique(final String statementName, final Object newValue, final Object oldValue) {
		Asserts.hasText(statementName, "statementName不能为空");
		Asserts.notNull(newValue, "newValue不能为空");

		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}

		Object object = get(statementName, newValue);
		return (object == null);
	}

	/**
	 * 获取声明名称.
	 */
	private String getStatementName(final String statementName) {
		Asserts.hasText(statementName, "statementName不能为空");
		return entityClass.getSimpleName() + "." + statementName;
	}
}
