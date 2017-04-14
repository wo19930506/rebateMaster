package com.edm.utils.execute;

import org.apache.commons.lang.StringUtils;

import com.edm.utils.TypeMap;
import com.edm.utils.web.Validates;

public class Imports {
	
	public static boolean isEmail(String email) {
		if (StringUtils.isBlank(email) || email.length() > TypeMap.EMAIL_LENGTH || !Validates.email(email)) {
			return false;
		}
		return true;
	}
		
}
