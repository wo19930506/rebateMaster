package com.edm.utils;

import org.apache.commons.lang.StringUtils;

import com.edm.utils.consts.Value;
import com.edm.utils.web.Validates;

public class Values {

	public static final Integer get(Integer value) {
		return get(value, Value.I);
	}
	
	public static final Integer get(Integer value, int defaultValue) {
		return value != null ? value : defaultValue;
	}
	
	public static final Long get(Long value) {
		return get(value, Value.L);
	}
	
	public static final Long get(Long value, long defaultValue) {
		return value != null ? value : defaultValue;
	}

	public static final String get(String value) {
		return get(value, Value.S);
	}
	
	public static final String get(String value, String defaultValue) {
		return StringUtils.isNotBlank(value) ? value : defaultValue;
	}

    public static final String get(Integer value, String defaultValue) {
        return value != null ? String.valueOf(value) : defaultValue;
    }
    
    public static final String get(Long value, String defaultValue) {
        return value != null ? String.valueOf(value) : defaultValue;
    }
    
    public static final String get(Float value, String defaultValue) {
        return value != null ? String.valueOf(value) : defaultValue;
    }
	
	public static Integer integer(String value) {
		Integer i = null;
		if (StringUtils.isNotBlank(value) && Validates.integer(value)) {
			i = Integer.valueOf(value);
		}
		return i;
	}
}
