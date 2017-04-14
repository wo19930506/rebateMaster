package com.edm.app.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.edm.entity.Menu;
import com.edm.entity.Role;

/**
 * 实现SpringSecurity的UserDetailsService接口, 实现获取用户Detail信息的回调函数.
 * 
 * @author yjli
 */
public class Users extends User {

    private static final long serialVersionUID = -2350224684799582831L;

    private int corpId;
    private int corpParentId;
    private boolean senderValidate;
    private boolean openSms;
    private boolean isTriger;
    private boolean isJoin;
    private Integer permissionId;

    private List<Menu> menus;

    private Role role;

    public Users(String username, String password, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
    }

    public int getCorpId() {
        return corpId;
    }

    public void setCorpId(int corpId) {
        this.corpId = corpId;
    }

    public int getCorpParentId() {
        return corpParentId;
    }

    public void setCorpParentId(int corpParentId) {
        this.corpParentId = corpParentId;
    }

    public boolean isSenderValidate() {
        return senderValidate;
    }

    public void setSenderValidate(boolean senderValidate) {
        this.senderValidate = senderValidate;
    }

    public boolean isOpenSms() {
        return openSms;
    }

    public void setOpenSms(boolean openSms) {
        this.openSms = openSms;
    }

    public boolean isTriger() {
        return isTriger;
    }

    public void setTriger(boolean isTriger) {
        this.isTriger = isTriger;
    }

    public boolean isJoin() {
        return isJoin;
    }

    public void setJoin(boolean isJoin) {
        this.isJoin = isJoin;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}