package com.edm.app.security;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.edm.utils.Converts;
import com.edm.utils.consts.RoleMap;

/**
 * SpringSecurity的工具类.
 * 
 * @author xiaobo
 */
public class Securitys {

	/**
	 * 获取当前用户, 返回值为SpringSecurity的User类或其子类, 如果当前用户没有登录则返回null.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Users> T getCurrentUser() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof Users) {
				return (T) principal;
			}
		}

		return null;
	}

	/**
	 * 获取当前用户的登录名, 如果当前用户没有登录则返回空字符串.
	 */
	public static String getCurrentUserName() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			return authentication.getName();
		}

		return "";
	}

	/**
	 * 获取当前用户登录IP, 如果当前用户没有登录则返回空字符串.
	 */
	public static String getCurrentUserIp() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			Object details = authentication.getDetails();
			if (details instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
				return webDetails.getRemoteAddress();
			}
		}

		return "";
	}

	/**
	 * 判断当前用户是否拥有角色, 如果用户拥有参数中的任意一个角色则返回true.
	 */
	public static boolean hasAnyRole(String[] roles) {
		Authentication authentication = getAuthentication();
		Collection<GrantedAuthority> auths = authentication.getAuthorities();
		for (String role : roles) {
			for (GrantedAuthority auth : auths) {
				if (role.equals(auth.getAuthority())) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * 获取当前用户拥有的角色ID.
	 */
	public static Integer[] getCurrentRoleIds() {
		Authentication authentication = getAuthentication();
		Collection<GrantedAuthority> auths = authentication.getAuthorities();

		StringBuffer sbff = new StringBuffer();
		for (GrantedAuthority auth : auths) {
			Integer roleId = RoleMap.getId(auth.getAuthority());
			sbff.append(roleId).append(",");
		}

		return Converts._toIntegers(sbff.toString());
	}

	/**
	 * 将UserDetails保存到Security Context.
	 * 
	 * @param userDetails 已初始化好的用户信息.
	 * @param request 用于获取用户IP地址信息.
	 */
	public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request) {
		PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
				userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		
		authentication.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * 获取Authentication, 如果当前SecurityContext为空时返回null.
	 */
	private static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			return context.getAuthentication();
		}

		return null;
	}
}
