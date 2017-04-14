package com.edm.entity;

public class RoleMenu {
	
	private Integer roleMenuId;
	
	private Integer roleId; // 角色表主键
	
	private Integer menuId; // 菜单表主键			
		
	private String funcAuth; // 菜单功能增删查改权限,查询=1、增加=2、修改=3、删除=4、监管=5、导出＝6、
	
	public Integer getRoleMenuId() {
		return roleMenuId;
	}

	public void setRoleMenuId(Integer roleMenuId) {
		this.roleMenuId = roleMenuId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getFuncAuth() {
		return funcAuth;
	}

	public void setFuncAuth(String funcAuth) {
		this.funcAuth = funcAuth;
	}

}
