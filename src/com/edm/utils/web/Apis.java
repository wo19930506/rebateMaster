package com.edm.utils.web;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Apis {

	private static Logger logger = LoggerFactory.getLogger(Apis.class);
	
	public static String get(String action) {
	    return get(action, (Integer) null);
	}
	
	public static String get(String action, Integer timeout) {
		HttpClient client = null;
		String status = null;
		try {
			client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000);
            if (timeout != null) {
                HttpConnectionParams.setSoTimeout(client.getParams(), timeout);
            }
			HttpGet get = new HttpGet(action);
			HttpResponse response = client.execute(get);
			status = response.getStatusLine().toString();
			get.abort();
			logger.info("(Api:get) action: " + action + ", status: " + status);
		} catch (Exception e) {
			logger.error("(Api:get) error: ", e);
		} finally {
			client.getConnectionManager().shutdown();
		}

		return status;
	}
	
	public static String get(String action, String encoding) {
		HttpClient client = null;
		String content = null;
		try {
			client = new DefaultHttpClient();
			HttpGet get = new HttpGet(action);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = IOUtils.toString(entity.getContent(), encoding);
			}
			get.abort();
			logger.info("(Api:get) action: " + action);
		} catch (Exception e) {
			logger.error("(Api:get) error: ", e);
		} finally {
			client.getConnectionManager().shutdown();
		}

		return content;
	}
}
