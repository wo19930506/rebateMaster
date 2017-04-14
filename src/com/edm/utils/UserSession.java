package com.edm.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.edm.app.security.Securitys;
import com.edm.app.security.Users;
import com.edm.entity.Menu;
import com.edm.entity.Role;
import com.edm.modules.orm.MapBean;
import com.edm.utils.consts.PermissionMap;
import com.edm.utils.consts.Permissions;
import com.edm.utils.consts.RoleMap;
import com.edm.utils.consts.RoleType;
import com.edm.utils.web.Pages;

public class UserSession {

	/**
	 * 返回当前登录用户.
	 */
	public static final Users getUser() {
		return Securitys.getCurrentUser();
	}

	/**
	 * 返回当前登录用户的账号.
	 */
	public static final String getUserId() {
		return Securitys.getCurrentUser().getUsername();
	}

	/**
	 * 设置当前登录用户的账号(搜索)
	 */
	public static final String containsUserId() {
		String userId = getUserId();
		
        Role role = getUser().getRole();
        if (Asserts.hasAny(role.getType(), new Integer[] { RoleType.MGR })
                || role.getRoleName().equals("审核员")) {
            userId = null;
        }
//		if (Securitys.hasAnyRole(new String[] { RoleMap.ADM.getName(), RoleMap.MGR.getName() })) {
//            userId = null;
//      }
		return userId;
	}
	
	/**
	 * 返回当前登录用户的企业ID.
	 */
	public static final int getCorpId() {
		return Securitys.getCurrentUser().getCorpId();
	}
	
    /**
     * 返回当前登录用户的父企业ID.
     */
    public static final int getCorpParentId() {
        return Securitys.getCurrentUser().getCorpParentId();
    }
    
    /**
     * 设置当前登录用户的机构ID(搜索)
     */
    public static final Integer containsCorpId() {
        Integer corpId = null;
        if (!Securitys.hasAnyRole(new String[] { RoleMap.ADM.getName(), RoleMap.MGR.getName() })) {
            corpId = UserSession.getCorpId();
        }
        return corpId;
    }

	/**
	 * 返回当前登录用户的IP.
	 */
	public static final String getUserIp() {
		return Securitys.getCurrentUserIp();
	}
	
	/**
	 * 返回当前登录用户的Api触发.
	 */
	public static final boolean isTriger() {
		return Securitys.getCurrentUser().isTriger();
	}
	
	/**
     * 返回当前登录用户的Api群发.
     */
    public static final boolean isJoin() {
        return Securitys.getCurrentUser().isJoin();
    }
	
	/**
	 * 若当前角色为ADM|MGR|OPT|BUK|BSN,则返回corpId
	 */
	public static final Integer access() {
		Integer corpId = null;
		if (Securitys.hasAnyRole(new String[] { RoleMap.ADM.getName(), RoleMap.MGR.getName(), RoleMap.BUK.getName(), RoleMap.ALL.getName() })) {
			corpId = getCorpId();
		}

		return corpId;
	}
	
	/**
	 * 若当前角色为RDR, 返回当前corpId
	 */
	public static final Integer bindCorpId() {
		Integer bindCorpId = null;
		if (hasAnyPermission(new String[] { Permissions.RDR.getName() })) {
			bindCorpId = getCorpId();
		}

		return bindCorpId;
	}
	
	/**
	 * 返回当前登录用户的发件人验证.
	 */
	public static final boolean senderValidate() {
	    return getUser().isSenderValidate();
	}
	
	/**
     * 返回当前登录用户的短信通知设置.
     */
	public static final boolean openSms() {
	    return getUser().isOpenSms();
	}
	
	 /**
     * 按权限设置当前登录用户的账号(搜索)
     */
    public static final String loadUserId(HttpServletRequest request, PermissionMap permission) {
        String userId = getUserId();
        if (hasPermission(request, permission)) {
            userId = null;
        }

        return userId;
    }
	
    /**
     * 是否有权限.
     */
    public static final boolean hasPermission(HttpServletRequest request, PermissionMap permission) {
        String href = request.getServletPath();
        List<Menu> menuList = getUser().getMenus();
        if (Asserts.empty(menuList)) {
            return false;
        }

        for (Menu menu : menuList) {
            if (href.equals(menu.getUrl())) {
                Integer[] permissions = menu.getFuncAuths();
                if (Asserts.hasAny(PermissionMap.A.getId(), permissions)) {
                    return true;
                }
                if (Asserts.hasAny(permission.getId(), permissions)) {
                    return true;
                }
                return false;
            }
        }

        return false;
    }
    
    public static final boolean hasAnyPermission(String[] permissionNames) {
        Integer permissionId = getUser().getPermissionId();
        if (permissionId == null) {
            return false;
        }
        String permissionName = Permissions.getName(permissionId);
        if (StringUtils.isBlank(permissionName)) {
            return false;
        }
        if (Asserts.hasAny(permissionName, permissionNames)) {
            return true;
        }
        return false;
    }
    
	/**
	 * 是否是管理员
	 */
	public static final boolean isManager() {
		Integer type = getUser().getRole().getType();
		Integer roleId = getUser().getRole().getRoleId();
		boolean isMsg = RoleType.MGR.equals(type) || (RoleType.ADM.equals(type) && roleId==1);
		return isMsg;
	}
	
	/**
     * 若role in [ADM,MGR], corpId为可选, 则corpId=当前登录机构ID. 
     */
    public static Integer getCorpId(MapBean mb, Integer corpId) {
        if (Securitys.hasAnyRole(new String[] { RoleMap.ADM.getName(), RoleMap.MGR.getName() })) {
            if (corpId != null) {
                Pages.search(mb);
                mb.put("corpId", corpId);
            }
        } else {
            corpId = UserSession.getCorpId();
        }
        return corpId;
    }
}
