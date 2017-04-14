package com.edm.app.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.edm.app.auth.Auth;
import com.edm.modules.cache.Ehcache;

public class ActiveJob {

	private static final Logger logger = LoggerFactory.getLogger(ActiveJob.class);

	@Autowired
	private Ehcache ehcache;


	public void execute() {
		if (!Auth.isSetup()) {
			return;
		}

		try {
			/*
			 * 逻辑操作
			 */
		} catch (Exception e) {
			logger.error("(ActiveJob:execute) error: ", e);
		}
	}

	
}