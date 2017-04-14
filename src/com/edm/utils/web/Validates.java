package com.edm.utils.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.edm.utils.MailBox;

public class Validates {
	
	/** IP正则表达式 */
	public static final String REGEX_IP = "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$";
	
	public static boolean clean(Object target) {
		if (isTarget(target)) {
			if (!Clean.clean((String) target)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean required(Object target) {
		if (target == null || (target instanceof String && StringUtils.isBlank((String) target))) {
			return false;
		}
		return true;
	}
	
	public static boolean integer(Object target) {
		return number(target, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static boolean biginteger(Object target) {
		return number(target, Long.MIN_VALUE, Long.MAX_VALUE);
	}
	
	private static boolean number(Object target, Object min, Object max) {
		if (isTarget(target)) {
			String str = (String) target;
			if (matches(str, "^-?\\d+$")) {
				boolean opt = StringUtils.startsWith(str, "-");
				int len = String.valueOf(max).length() + (opt ? 1 : 0);
				if (str.length() < len) {
					return true;
				}
				if (str.length() == len && str.compareTo(String.valueOf((opt ? min : max))) <= 0) {
					return true;
				}
			}
			
			return false;
		}
		return true;
	}
	
	public static boolean date(Object target) {
		return formatter(target, "yyyy-MM-dd");
	}
	
	public static boolean datetime(Object target) {
		return formatter(target, "yyyy-MM-dd HH:mm");
	}
	
    public static boolean formatter(Object target, String pattern) {
        if (isTarget(target)) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            try {
                fmt.parseDateTime((String) target);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
	
	public static boolean eq(Object target, String value) {
		if (isTarget(target)) {
			return StringUtils.equals((String) target, value);
		}
		return true;
	}
	
	public static boolean email(Object target) {
		if (isTarget(target)) {
			return MailBox.validate((String) target);
		}
		return true;
	}
	
	public static boolean length(Object target, int[] range) {
		if (target != null && target instanceof String && StringUtils.isNotBlank((String) target)) {
			int min = 0;
			int max = Integer.MAX_VALUE;
			if (range.length >= 1) {
				min = range[0];
			}
			if (range.length >= 2) {
				max = range[1];
			}

			String str = (String) target;
			if (str.length() < min || str.length() > max) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean size(Object target, int[] range) {
		if (target != null) {
			int min = 0;
			int max = Integer.MAX_VALUE;
			if (range.length >= 1) {
				min = range[0];
			}
			if (range.length >= 2) {
				max = range[1];
			}

			Integer i = null;
			if (target instanceof String) {
				if (NumberUtils.isDigits((String) target)) {
					i = Integer.valueOf((String) target);
				} else {
					return false;
				}
			} else if (target instanceof Integer) {
				i = (Integer) target;
			}
			
			if (i != null && (i < min || i > max)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean matches(Object target, String regex) {
		if (isTarget(target)) {
			return pattern(target, regex).matches();
		}
		return true;
	}
	
	public static boolean notMatches(Object target, String regex) {
		if (isTarget(target)) {
			return !pattern(target, regex).matches();
		}
		return true;
	}
	
	public static Matcher pattern(Object target, String regex) {
		final Matcher m = Pattern.compile(regex, Pattern.MULTILINE + Pattern.DOTALL).matcher((String) target);
		return m;
	}
	
	public static boolean isTarget(Object target) {
		return target != null && (target instanceof String && StringUtils.isNotBlank((String) target));
	}
}
