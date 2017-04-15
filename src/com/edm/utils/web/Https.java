package com.edm.utils.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author xiaobo
 * @see 根据validators验证条件和参数名称来获取request请求中的参数值
 */
public class Https {

	public static String getStr(HttpServletRequest request, String name, Object... validators) {
		String value = StringUtils.trim(request.getParameter(name));
		return (String) Validator.validate(value, validators);
	}
	
	public static Integer getInt(HttpServletRequest request, String name, Object... validators) {
		String value = StringUtils.trim(request.getParameter(name));
		value = (String) Validator.validate(value, validators);
		return StringUtils.isBlank(value) ? null : Integer.valueOf(value);
	}
}
