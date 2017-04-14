package com.edm.utils;

import org.apache.commons.lang.StringUtils;

import com.edm.utils.except.Errors;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

public class Mongos {

	private Mongo mongo;

	private Mongos() {
	}

	private static class Holder {
		private static final Mongos INSTANCE = new Mongos();
	}

	public static Mongos getInstance() {
		return Holder.INSTANCE;
	}

	public void init(String host, int port, int poolSize) {
		if (StringUtils.isBlank(host)) {
			throw new Errors("host is blank");
		}
		try {
			if (mongo == null) {
				mongo = new Mongo(host, port);
				MongoOptions opt = mongo.getMongoOptions();
				opt.autoConnectRetry = true;
				opt.connectionsPerHost = poolSize;
			}
		} catch (Exception e) {
			throw new Errors("(Mongos:init) error: ", e);
		}
	}

	public Mongo getMongo() {
		if (mongo == null) {
			throw new Errors("mongo is null");
		}
		
		return mongo;
	}

	public static DB db(String db) {
		return getInstance().getMongo().getDB(db);
	}
}
