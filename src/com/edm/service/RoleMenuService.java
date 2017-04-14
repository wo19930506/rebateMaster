package com.edm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edm.dao.Dao;
import com.edm.entity.RoleMenu;
import com.edm.modules.orm.MapBean;

@Transactional
@Service
public class RoleMenuService {

	@Autowired
    private Dao dao;
	
	public List<RoleMenu> find(Integer roleId) {
		MapBean mb = new MapBean();
		mb.put("roleId", roleId);
		return dao.find("RoleMenu.query", mb);
	}
	
}
