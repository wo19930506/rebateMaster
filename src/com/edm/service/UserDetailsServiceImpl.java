package com.edm.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.edm.app.auth.Auth;
import com.edm.app.security.Users;
import com.edm.entity.Menu;
import com.edm.entity.Role;
import com.edm.entity.RoleMenu;
import com.edm.entity.User;
import com.edm.utils.Asserts;
import com.edm.utils.Converts;
import com.edm.utils.Values;
import com.edm.utils.consts.RoleMap;
import com.edm.utils.consts.Status;
import com.edm.utils.consts.Value;
import com.edm.utils.except.AuthErrors;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws AuthenticationException, DataAccessException {
		if (!Auth.isSetup()) {
			throw new AuthErrors("禁止登录系统！");
		}
//		if (Auth.isAuth() && !Auth.expire(Ntps.get())) {
//			throw new AuthErrors("证书已过期！");
//		}
		if (Auth.isAuth()) {
            throw new AuthErrors("证书已过期！");
        }
		
//		Integer[] roleIds = { RoleMap.ADM.getId(), RoleMap.MGR.getId(), RoleMap.BUK.getId(), RoleMap.BSN.getId(), RoleMap.RDR.getId() };
		Integer[] roleIds = { RoleMap.SEE.getId() };
		
		User user = userService.get(username);
		
		if (user == null || Asserts.hasAny(user.getRoleId(), roleIds)) {
			throw new AuthErrors("账号输入有误！");
		}
		if (!user.getStatus().equals(Status.ENABLED)) {
			throw new AuthErrors("账号已失效！");
		}
		
		
		Set<GrantedAuthority> grantedAuths = obtainGrantedAuthorities(user);

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		Users users = new Users(user.getUserId(), user.getPassword(), 
				enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuths);
		
/*		users.setTriger(trigerService.isApi(user.getCorpId(), user.getUserId()));*/
/*		users.setJoin(Values.get(corp.getJoinApi()).equals(Value.T) ? true : false);
        users.setSenderValidate(Values.get(corp.getSenderValidate()).equals(1) ? true : false);*/
        users.setOpenSms(Values.get(user.getOpenSms()).equals(Value.T) ? true : false);
        users.setPermissionId(user.getPermissionId());
        createPermission(users, user.getRoleId());
        
		logger.info("(Login:load) username: " + users.getUsername() + ", corp: " + users.getCorpId() + ", triger: " + users.isTriger() + ", join: " + users.isJoin() + ", sender_validate: " + users.isSenderValidate());

		return users;
	}

    private Set<GrantedAuthority> obtainGrantedAuthorities(User user) {
        Set<GrantedAuthority> authSet = Sets.newHashSet();
        String roleName = RoleMap.getName(user.getRoleId());
        // bad code...
        if (StringUtils.isBlank(RoleMap.getName(user.getRoleId()))) {
            roleName = RoleMap.ALL.name();
        }
        authSet.add(new GrantedAuthorityImpl(user.getPrefixedRole(roleName)));
        return authSet;
    }
	
    private void createPermission(Users users, Integer roleId) {
        Role role = roleService.get(roleId);
        if (role == null) {
            throw new AuthErrors("角色已失效！");
        }
        users.setRole(role);

        List<RoleMenu> roleMenuList = roleMenuService.find(roleId);
        String menuIdStr = "";
        for (RoleMenu mr : roleMenuList) {
            menuIdStr += mr.getMenuId() + ",";
        }
        Integer[] menuIds = Converts._toIntegers(menuIdStr);
        // 菜单
        List<Menu> menuList = menuService.find(menuIds);

        List<Menu> removeMenuList = Lists.newArrayList();
        for (Menu m : menuList) {
            for (RoleMenu rm : roleMenuList) {
                if (m.getMenuId().equals(rm.getMenuId())) {
                    String funcAuth = rm.getFuncAuth();
                    m.setFuncAuths(Converts._toIntegers(funcAuth));
                }
            }
            if (!StringUtils.isBlank(m.getUrl())
                    && m.getUrl().equals("account/sender/page")
                    && !users.isSenderValidate()) {
                removeMenuList.add(m);
            }
            if (!users.isTriger() && !users.isJoin()
                    && !StringUtils.isBlank(m.getUrl())
                    && m.getUrl().equals("report/task/page?m=join")) {
                removeMenuList.add(m);
            } else {
                if (!StringUtils.isBlank(m.getUrl())
                        && m.getUrl().equals("report/task/page?m=join")) {
                    if (users.isTriger()) {
                        m.setUrl("report/triger/page");
                    }
                    if (users.isJoin()) {
                        m.setUrl("report/task/page?m=join");
                    }
                }
            }
        }
        for (Menu removeMenu : removeMenuList) {
            menuList.remove(removeMenu);
        }
        users.setMenus(menuList);
    }
}
