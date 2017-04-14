package com.edm.modules.orm;

import java.util.HashMap;

/**
 * MapBean类.
 * 
 * @author yjli
 */
public class MapBean extends HashMap<String, Object> {

	private static final long serialVersionUID = -3343906360119730930L;

	public MapBean() {
	}

	/**
	 * MapBean构造函数.
	 * 
	 * eg. MapBean mb = new MapBean("id", 1, "name", "test");
	 */
	public MapBean(Object... props) {
		put(props);
	}

	/**
	 * 获取Integer.
	 */
	public Integer getInteger(Object key) {
		return (Integer) get(key);
	}

	/**
	 * 获取Integer, 自定义默认值.
	 */
	public Integer getInteger(Object key, int defaultValue) {
		Integer i = (Integer) get(key);
		return (i == null ? defaultValue : i);
	}

	/**
	 * 获取Long.
	 */
	public Long getLong(Object key) {
		return (Long) get(key);
	}

	/**
	 * 获取Long, 自定义默认值.
	 */
	public Long getLong(Object key, long defaultValue) {
		Long i = (Long) get(key);
		return (i == null ? defaultValue : i);
	}
	
	/**
	 * 获取String.
	 */
	public String getString(Object key) {
		return (String) get(key);
	}

	/**
	 * 获取String, 自定义默认值.
	 */
	public String getString(Object key, String defaultValue) {
		String str = (String) get(key);
		return (str == null ? defaultValue : str);
	}

	/**
	 * 设置键值对.
	 * 
	 * eg. put("id", 1, "name", "test");
	 */
	public void put(Object... props) {
		for (int i = 1; i < props.length; i += 2) {
			put(String.valueOf(props[i - 1]), props[i]);
		}
	}
	
	public static void main(String[] args) {
        MapBean mb = new MapBean();
        mb.put("id", 1, "name");
        System.out.println(mb);
    }
}
