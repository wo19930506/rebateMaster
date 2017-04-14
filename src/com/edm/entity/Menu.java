package com.edm.entity;

import java.io.Serializable;
import java.util.Date;

public class Menu implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//主菜单
	public final static int MENU_DATASOURCE = 1;//数据管理
	public final static int MENU_MAIL = 2;//邮件管理
	public final static int MENU_REPORT = 3;//数据报告
	public final static int MENU_ACCOUNT = 4;//账户管理
	
	//数据管理
	public final static int MENU_DATASOURCE_TAG_ID = 5;//收件人
	public final static int MENU_DATASOURCE_PROP_ID = 6;//属性
	public final static int MENU_DATASOURCE_FILTER_ID = 7;//过滤器
	public final static int MENU_DATASOURCE_SELECTION_ID = 8;//筛选
	public final static int MENU_DATASOURCE_FORM_ID = 9;//表单
	//邮件管理
	public final static int MENU_MAIL_TEMPLATE_ID = 10;//模板
	public final static int MENU_MAIL_TASK_ID = 11;//任务
	public final static int MENU_MAIL_CAMPAIGN_ID = 12;//活动
	public final static int MENU_MAIL_AUDIT_ID = 13;//审核
	//数据报告
	public final static int MENU_REPORT_LOCAL_ID = 14;//总览
	public final static int MENU_REPORT_TASK_ID = 15;//任务
	public final static int MENU_REPORT_API_ID = 16;//API
	public final static int MENU_REPORT_CAMPAIGN_ID = 17;//活动
	public final static int MENU_REPORT_TOUCH_ID = 18;//触发计划
	//账户管理
	public final static int MENU_ACCOUNT_USER_ID = 21;//子账号
	
	/**
	 * 对应角色页面菜单功能id
	 */
	//数据管理
	public final static String MENU_DATASOURCE_TAG = "M_1";//收件人管理
	public final static String MENU_DATASOURCE_PROP = "M_2";//属性管理
	public final static String MENU_DATASOURCE_FILTER = "M_3";//过滤器管理
	public final static String MENU_DATASOURCE_SELECTION = "M_4";//收件人筛选与导出
	public final static String MENU_DATASOURCE_FORM = "M_5";//表单管理
	//邮件管理
	public final static String MENU_MAIL_TEMPLATE_ADD = "M_6";//模板设计与管理
	public final static String MENU_MAIL_TEMPLATE_QUERY = "M_7";//模板预览
	public final static String MENU_MAIL_TASK = "M_8";//任务创建
	public final static String MENU_MAIL_AUDIT = "M_9";//任务审核
	public final static String MENU_MAIL_TASK_CONTROL = "M_10";//任务投递监控管理
	public final static String MENU_MAIL_CAMPAIGN = "M_11";//活动创建与管理
	//数据报告
	public final static String MENU_REPORT_QUERY = "M_12";//报告查阅
	public final static String MENU_REPORT_EXPORT = "M_13";//报告导出
	//账户管理
	public final static String MENU_ACCOUNT_USER = "M_14";//管理员角色(默认具备子账号管理功能)
	
	private Integer menuId;
	
	private Integer parentId; // 父节点
	
	private String name; // 菜单功能名称
	
	private String url; // 菜单url
	
	private Date modifyTime;
	
	private String dataCate; // 节点英文目录名
	
	private Integer sort; // 排序号
	
	private Integer[] funcAuths;
	
	private String funcAuth;//菜单功能增删查改权限，0=全部功能，（1,2,3,4,5,6）=查询、增加、修改、删除、监管、导出
	private Integer roleId;
	
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataCate() {
		return dataCate;
	}

	public void setDataCate(String dataCate) {
		this.dataCate = dataCate;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getFuncAuth() {
		return funcAuth;
	}

	public void setFuncAuth(String funcAuth) {
		this.funcAuth = funcAuth;
	}

	public Integer[] getFuncAuths() {
		return funcAuths;
	}

	public void setFuncAuths(Integer[] funcAuths) {
		this.funcAuths = funcAuths;
	}

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}
