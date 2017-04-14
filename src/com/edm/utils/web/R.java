package com.edm.utils.web;

public class R {

	public static final Integer CLEAN = 0; 		// XSS过滤
	public static final Integer EQ = 1;			// 值相等
	public static final Integer DATE = 2; 		// 日期 (yyyy-MM-dd)
	public static final Integer DATETIME = 3; 	// 日期时间 (yyyy-MM-dd HH:mm)
	public static final Integer INTEGER = 4; 	// 整数值
	public static final Integer LONG = 5; 		// 长整数值
	public static final Integer LENGTH = 6; 	// 字符串区间
	public static final Integer LIKE = 7;		// 值相似
	public static final Integer MAIL = 8; 		// 邮箱
	public static final Integer REGEX = 9; 		// 正则
	public static final Integer REQUIRED = 10; 	// 必填
	public static final Integer SIZE = 11; 		// 数值区间
	public static final Integer NONE = -1;		// 可为空
}
