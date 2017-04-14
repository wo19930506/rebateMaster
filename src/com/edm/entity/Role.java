package com.edm.entity;

import java.util.Date;
import java.util.List;

public class Role {
	
	private Integer roleId;
	
	private String roleName; // 角色名称
	
	private String roleDesc; // 角色描述		
	
	private Date createTime; // 创建时间
	
	private Date modifyTime; // 操作时间
		
	private String operator; // 操作者
	
	private Integer type; // 0-->前台普通角色；1-->前台管理员；2-->后台角色
	
	private List<Menu> menuList;//角色拥有的菜单列表

	private List<String> menuFuncList;//角色拥有的功能列表
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<String> getMenuFuncList() {
		return menuFuncList;
	}

	public void setMenuFuncList(List<String> menuFuncList) {
		this.menuFuncList = menuFuncList;
	}
	
}
