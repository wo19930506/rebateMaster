package com.edm.utils.web;

import org.apache.commons.lang.StringUtils;

import com.edm.utils.Converts;
import com.edm.utils.except.Errors;

/**
 * Validator工具类.
 * 
 * @author yjli
 */
public class Validator {

    public static String getStr(Object target, Object... validators) {
        return (String) Validator.validate(target, validators);
    }
    
    public static Integer getInt(Object target, Object... validators) {
        String value = (String) Validator.validate(target, validators);
        return StringUtils.isBlank(value) ? null : Integer.valueOf(value);
    }
    
	/**
	 * 校验函数, 支持动态校验规则.
	 * 
	 * eg.
	 * validate(target, R.CLEAN, R.REQUIRED, R.REGEX, "message", "regex:^(hello|world)$:error");
	 * validate(target, R.CLEAN, R.EQ, "eq:hello");
	 * validate(target, R.CLEAN, R.INTEGER, R.SIZE, "{1,100}", "message");
	 * 
	 * @param target 校验对象
	 * @param validators 可变的validators校验规则
	 * @return 返回校验对象
	 */
	public static Object validate(Object target, Object... validators) {
		if (validators != null && validators.length > 0) {
			String message = null;
			String eq = null;
			String regex = null;
			String error = null;
			boolean matches = false;
			int[] range = null;
			
			for (Object validator : validators) {
				if (validator instanceof String) {
					String value = (String) validator;
					if (StringUtils.startsWith(value, "{") && StringUtils.endsWith(value, "}")) {
						String r = StringUtils.removeEnd(StringUtils.removeStart(value, "{"), "}");
						range = Converts._toInts(r);
					} else if (StringUtils.startsWith(value, "eq:")) {
						eq = StringUtils.removeStart(value, "eq:");
					} else if (StringUtils.startsWith(value, "regex:") || StringUtils.startsWith(value, "!regex:")) {
						String r = null;
						if (StringUtils.startsWith(value, "regex:")) {
							r = StringUtils.removeStart(value, "regex:");
							matches = true;
						} else {
							r = StringUtils.removeStart(value, "!regex:");
						}
						regex = StringUtils.substringBefore(r, ":");
						error = StringUtils.substringAfter(r, ":");
					} else {
						message = value;
					}
				}
			}

			boolean throwable = false;
			
			if (StringUtils.isNotBlank(message) || StringUtils.isNotBlank(error)) {
				throwable = true;
			}
			
			if (StringUtils.isBlank(message)) {
				message = "";
			}
			
			if (StringUtils.isBlank(error)) {
				error = "";
			}
			
			for (Object validator : validators) {
				if (validator instanceof Integer) {
					if (validator == R.REQUIRED && !Validates.required(target)) {
						return throwable(throwable, message + "不能为空值");
					} else if (validator == R.LENGTH && !Validates.length(target, range)) {
						return throwable(throwable, message + "的字符长度只能介于" + range[0] + "到" + range[1] + "之间");
					} else if (validator == R.SIZE && !Validates.size(target, range)) {
						return throwable(throwable, message + "的值只能介于" + range[0] + "到" + range[1] + "之间");
					} else if (validator == R.INTEGER && !Validates.integer(target)) {
						return throwable(throwable, message + "不是合法的整数值");
					} else if (validator == R.LONG && !Validates.biginteger(target)) {
						return throwable(throwable, message + "不是合法的长整数值");
					} else if (validator == R.DATE && !Validates.date(target)) {
						return throwable(throwable, message + "不是合法的日期");
					} else if (validator == R.DATETIME && !Validates.datetime(target)) {
						return throwable(throwable, message + "不是合法的日期时间");
					} else if (validator == R.EQ && !Validates.eq(target, eq)) {
						return throwable(throwable, message + "不一致");
					} else if (validator == R.MAIL && !Validates.email(target)) {
						return throwable(throwable, message + "不是合法的邮件格式");
					} else if (validator == R.CLEAN) {
						if (!Validates.clean(target)) {
							return throwable(throwable, message + "不能包含“<span style='color: #272727;'>" + XSS.clean((String) target) + "</span>”字符");
						}
					} else if (validator == R.REGEX) {
						if (matches) {
							if (!Validates.matches(target, regex)) {
								return throwable(throwable, message + error);
							}
						} else {
							if (!Validates.notMatches(target, regex)) {
								return throwable(throwable, message + error);
							}
						}
					}
				}
			}
		}
		
		return target;
	}
	
	private static Object throwable(boolean throwable, String message) {
		if (throwable) {
			throw new Errors(message);
		}
		return null;
	}
}
