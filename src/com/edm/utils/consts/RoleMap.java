package com.edm.utils.consts;

/**
 * 角色.
 * 
 * @author xiaobo
 */
public enum RoleMap {

	ADM(1, "ROLE_ADM"), // 超级管理员	:ADMIN
	MGR(2, "ROLE_MGR"), // 管理员		:Manager
	BUK(3, "ROLE_BUK"), // 群发人员		:Bulk Mail
//	BSN(4, "ROLE_BSN"), // 运营专员		:Business
	SEE(5, "ROLE_SEE"), // 任务监控专员	:See
//	RDR(6, "ROLE_RDR"), // 企业只读人员	:Reader
	
	ALL(1000000, "ROLE_ALL"); // bad code...
	
	private final Integer id;
	private final String name;

	private RoleMap(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static final Integer getId(String roleName) {
		Integer roleId = null;
		for (RoleMap role : RoleMap.values()) {
			if (role.getName().equals(roleName)) {
				roleId = role.getId();
				break;
			}
		}
		return roleId;
	}

	public static final String getName(Integer roleId) {
		String name = "";
		for (RoleMap role : RoleMap.values()) {
		    if (role.getId().equals(roleId)) {
                name = role.name();
                break;
            }
		}
		return name;
	}
}
