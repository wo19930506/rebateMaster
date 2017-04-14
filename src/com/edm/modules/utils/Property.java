package com.edm.modules.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties文件工具类.
 */
public class Property {

	private static final Logger logger = LoggerFactory.getLogger(Property.class);

	private static Properties props = new Properties();
	
	private Property() {
	}
	
	/**
	 * 载入properties文件.
	 */
	public static Properties load(String... locations) {
		for (String location : locations) {
			InputStream input = null;
			try {
				input = new FileInputStream(location);
				props.load(input);
			} catch (IOException e) {
				logger.error("Could not load properties from classpath: {}, exception: {}", location, e.getMessage());
			} finally {
				if (input != null) {
					try { input.close(); } catch (IOException e) {}
				}
			}
		}
		
		return props;
	}
	
	/**
	 * 获取当前Properties.
	 */
	public static Properties instance() {
		return props;
	}
	
	/**
	 * 获取值.
	 */
	public static String getStr(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * 获取值.
	 */
	public static Integer getInt(String key) {
		String str = getStr(key);
		if (StringUtils.isNotBlank(str) && NumberUtils.isNumber(str) && !StringUtils.contains(str, ".")) {
			int len = String.valueOf(Integer.MAX_VALUE).length();
			if (str.length() < len) {
				return Integer.valueOf(str);
			}
			if (str.length() == len && str.compareTo(String.valueOf((StringUtils.startsWith(str, "-") ? Integer.MIN_VALUE : Integer.MAX_VALUE))) <= 0) {
				return Integer.valueOf(str);
			}
			return null;
		}
		return null;
	}
}
