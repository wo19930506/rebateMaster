package com.edm.utils;

/**
 * 属性类型.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum TypeMap {
	
	STRING		("String", "字符串"),
	INTEGER		("Integer", "整数"),
//	BIG_INTEGER	("Big integer", "长整数"),
	DATE		("Date (yyyy-MM-dd)", "日期 (yyyy-MM-dd)");
//	DATE_TIME	("Datetime (yyyy-MM-dd HH:mm:ss)", "日期时间 (yyyy-MM-dd HH:mm:ss)");

	public static final int STRING_LENGTH = 255;
	public static final int EMAIL_LENGTH = 64;
	
	private final String type;
	private final String desc;

	private TypeMap(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static final String getType(String desc) {
		for (TypeMap mapper : TypeMap.values()) {
			if (mapper.getDesc().equals(desc)) {
				return mapper.getType();
			}
		}
		return null;
	}

	public static final String getDesc(String type) {
		for (TypeMap mapper : TypeMap.values()) {
			if (mapper.getType().equals(type)) {
				return mapper.getDesc();
			}
		}
		return null;
	}
}
