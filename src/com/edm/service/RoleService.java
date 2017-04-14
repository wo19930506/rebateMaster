package com.edm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edm.dao.Dao;
import com.edm.entity.Menu;
import com.edm.entity.Role;
import com.edm.modules.orm.MapBean;
import com.edm.utils.Asserts;
import com.google.common.collect.Maps;

@Transactional
@Service
public class RoleService {

	@Autowired
    private Dao dao;
	@Autowired
	private MenuService menuService;
	
	public Role get(Integer roleId) {
		MapBean mb = new MapBean();
		mb.put("roleId", roleId);
		Role role = dao.get("Role.query", mb);
        Map<Integer, List<Menu>> menuMap = menuMap();
        menuList(role, menuMap);
		return role;
	}
    
    public List<Role> find(Integer type) {
        MapBean mb = new MapBean();
        mb.put("type", type);
        List<Role> roleList = dao.find("Role.query", mb);
        Map<Integer, List<Menu>> menuMap = menuMap();
        for (Role r : roleList) {
            menuList(r, menuMap);
        }
        return roleList;
    }
    
    public List<Role> find(Integer[] types) {
        MapBean mb = new MapBean();
        mb.put("types", types);
        List<Role> roleList = dao.find("Role.query", mb);
        Map<Integer, List<Menu>> menuMap = menuMap();
        for (Role r : roleList) {
            menuList(r, menuMap);
        }
        return roleList;
    }
	
	/**
	 * 菜单
	 * @param roleList
	 */
	private void menuList(Role role, Map<Integer, List<Menu>> menuMap) {
//		List<Menu> menuList = menuService.find(role.getRoleId());
	    List<Menu> menuList = menuMap.get(role.getRoleId());
	    if (!Asserts.empty(menuList)) {
    		role.setMenuList(menuList);
    		List<String> menuFuncList = this.showFuncList(menuList);
    		role.setMenuFuncList(menuFuncList);
	    }
	}
	
    private Map<Integer, List<Menu>> menuMap() {
        Map<Integer, List<Menu>> menuMap = Maps.newConcurrentMap();
        List<Menu> menuList = menuService.find();
        for (Menu menu : menuList) {
            List<Menu> menus = menuMap.get(menu.getRoleId());
            if (menus == null) {
                menus = new ArrayList<Menu>();
            }
            menus.add(menu);
            menuMap.put(menu.getRoleId(), menus);
        }
        return menuMap;
    }
	
	/**
	 * 菜单对应的功能项
	 * @param menuList
	 * @return
	 */
	private List<String> showFuncList(List<Menu> menuList) {
		List<String> list = new ArrayList<String>();
		for (Menu menu : menuList) {
			// 数据管理-收件人管理
			if (menu.getMenuId().equals(Menu.MENU_DATASOURCE_TAG_ID)) {
				list.add("数据管理-收件人管理");
			}
			// 数据管理-属性管理
			if (menu.getMenuId().equals(Menu.MENU_DATASOURCE_PROP_ID)) {
				list.add("数据管理-属性管理");
			}
			// 数据管理-过滤器管理
			if (menu.getMenuId().equals(Menu.MENU_DATASOURCE_FILTER_ID)) {
				list.add("数据管理-过滤器管理");
			}
			// 数据管理-收件人筛选与导出
			if (menu.getMenuId().equals(Menu.MENU_DATASOURCE_SELECTION_ID)) {
				list.add("数据管理-收件人筛选与导出");
			}
			// 数据管理-表单管理
			if (menu.getMenuId().equals(Menu.MENU_DATASOURCE_FORM_ID)) {
				list.add("数据管理-表单管理");
			}

			// 邮件管理-模板设计与管理
			if (menu.getMenuId().equals(Menu.MENU_MAIL_TEMPLATE_ID)
					&& menu.getFuncAuth().indexOf("0") != -1) {
				list.add("邮件管理-模板设计与管理");
				list.add("邮件管理-模板预览");
			}// 邮件管理-模板预览
			else if (menu.getMenuId().equals(Menu.MENU_MAIL_TEMPLATE_ID)
					&& menu.getFuncAuth().indexOf("1") != -1) {
				list.add("邮件管理-模板预览");
			}
			// 邮件管理-任务创建和任务投递监控管理
			if (menu.getMenuId().equals(Menu.MENU_MAIL_TASK_ID)
					&& menu.getFuncAuth().indexOf("0") != -1) {
				list.add("邮件管理-任务投递监控管理");
				list.add("邮件管理-任务创建");
			}// 邮件管理-任务投递监控管理
			else if (menu.getMenuId().equals(Menu.MENU_MAIL_TASK_ID)
					&& menu.getFuncAuth().indexOf("5") != -1) {
				list.add("邮件管理-任务投递监控管理");
			}// 邮件管理-任务创建
			else if (menu.getMenuId().equals(Menu.MENU_MAIL_TASK_ID)
					&& menu.getFuncAuth().indexOf("2") != -1) {
				list.add("邮件管理-任务创建");
			}
			// 邮件管理-审核
			if (menu.getMenuId().equals(Menu.MENU_MAIL_AUDIT_ID)) {
				list.add("邮件管理-审核");
			}
			// 邮件管理-活动创建与管理
			if (menu.getMenuId().equals(Menu.MENU_MAIL_CAMPAIGN_ID)) {
				list.add("邮件管理-活动创建与管理");
			}

			// 数据报告-导出
			if (menu.getMenuId().equals(Menu.MENU_REPORT_LOCAL_ID)
					&& menu.getFuncAuth().indexOf("0") != -1) {
				list.add("数据报告-查阅");
				list.add("数据报告-导出");
			}// 数据报告-查阅
			else if (menu.getMenuId().equals(Menu.MENU_REPORT_LOCAL_ID)
					&& menu.getFuncAuth().indexOf("1") != -1) {
				list.add("数据报告-查阅");
			}

			// 管理员-子账号
			if (menu.getMenuId().equals(Menu.MENU_ACCOUNT_USER_ID)) {
				list.add("管理员-子账号");
			}
		}
		return list;
	}
}
