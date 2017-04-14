package com.edm.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edm.dao.Dao;
import com.edm.entity.User;
import com.edm.modules.orm.MapBean;
import com.edm.modules.orm.Page;
import com.edm.utils.consts.Cnds;
import com.edm.utils.consts.Status;
import com.edm.utils.web.Pages;

@Transactional
@Service
public class UserService {

    @Autowired
    private Dao dao;
    
    public void save(User user, String orgUserId) {
        if (StringUtils.isNotBlank(orgUserId)) {
            dao.update("User.update", user);
        } else {
            dao.save("User.save", user);
        }
    }
    

    public User get(String userId) {
        return dao.get("User.query", new MapBean("userId", userId));
    }
    
    public User get(String managerId, String userId) {
        return dao.get("User.query", new MapBean("managerId", managerId, "userId", userId, "nameCnd", Cnds.EQ));
    }
    
    public User get(Integer[] corpIds, Integer corpId, String userId) {
        MapBean mb = new MapBean();
        Pages.put(mb, "corpIds", corpIds);
        Pages.put(mb, "corpId", corpId);
        mb.put("userId", userId);
        mb.put("nameCnd", Cnds.EQ);
        return dao.get("User.query", mb);
    }
    
    public long getCorpMgrCount(Integer corpId,Integer type){
        MapBean mb = new MapBean();
        Pages.put(mb, "corpId",corpId);
        Pages.put(mb, "type",type);
        return dao.countResult("User.corp", mb);
    }
    
    
    public List<User> get(List<Integer> corpIds) {
        MapBean mb = new MapBean();
        Pages.put(mb, "corpIds", corpIds);
        return dao.find("User.select",mb);
    }
    
    public User getByEmail(String email) {
        return dao.get("User.query", new MapBean("email", email));
    }

    public List<User> find(int corpId) {
    	Integer[] statuses = { Status.ENABLED, Status.FREEZE };
    	Integer[] notInRoleType = { 1, 2 };
		
    	MapBean mb = new MapBean();
		
    	Pages.put(mb, "corpId", corpId);
		Pages.put(mb, "notInRoleType", notInRoleType);
		Pages.put(mb, "statuses", statuses);
		Pages.order(mb, "createTime", Page.DESC);

		return dao.find("User.query", mb);
	}
    
    public List<User> find(MapBean mb, Integer[] corpIds, Integer corpId, String userId, Integer roleId, Integer[] statuses, String beginTime, String endTime, String nameCnd) {
        Pages.put(mb, "corpId", corpId);
        Pages.put(mb, "userId", userId);
    	Pages.put(mb, "roleId", roleId);
    	Pages.put(mb, "beginTime", beginTime);
        Pages.put(mb, "endTime", endTime);
        Pages.search(mb);
        Pages.put(mb, "statuses", statuses);
        Pages.put(mb, "nameCnd", nameCnd);
        Pages.put(mb, "corpIds", corpIds);
    	Pages.order(mb, "createTime", Page.DESC);
    	return dao.find("User.query", mb);
    }

    public boolean unique(String key, String value, String orgValue) {
        if (value == null || value.equals(orgValue)) {
            return true;
        }
        
        long count = dao.countResult("User.count", new MapBean(key, value));
        return count == 0;
    }
    
    public long children(Integer[] corpIds, Integer corpId) {
        Integer[] items = { Status.ENABLED, Status.FREEZE };
        MapBean mb = new MapBean();
        Pages.put(mb, "corpIds", corpIds);
        Pages.put(mb, "corpId", corpId);
        mb.put("statuses", items);
        return dao.countResult("User.count", mb);
    }
}
