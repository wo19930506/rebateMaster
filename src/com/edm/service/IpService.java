package com.edm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edm.dao.Dao;
import com.edm.entity.Ip;
import com.edm.modules.orm.MapBean;

@Transactional
@Service
public class IpService {

	@Autowired
	private Dao dao;

	public Ip get(String ip) {
		return dao.get("Ip.query", new MapBean("ip", ip));
	}
}
