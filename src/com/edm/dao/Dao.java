package com.edm.dao;

import org.springframework.stereotype.Repository;

import com.edm.modules.orm.ibatis.SimpleSqlMapClientDao;

@Repository
public class Dao extends SimpleSqlMapClientDao<Object, Integer> {
}
