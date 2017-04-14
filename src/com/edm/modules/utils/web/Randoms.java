package com.edm.modules.utils.web;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class Randoms {

	public static final String ALPHA_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	public static final String ALPHA_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA_NUMBER = "1234567890";
	
	public static String getRandomString(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(ALPHA_STRING.length());
			sb.append(ALPHA_STRING.charAt(number));
		}
		return sb.toString();
	}
	
	public static String getRandomUpperCase(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(ALPHA_UPPERCASE.length());
			sb.append(ALPHA_UPPERCASE.charAt(number));
		}
		return sb.toString();
	}
	
	public static String getRandomNumber(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(ALPHA_NUMBER.length());
			sb.append(ALPHA_NUMBER.charAt(number));
		}
		return sb.toString();
	}
	
	public static String getRandomString(String alphaString, String split) {
		if (StringUtils.isBlank(alphaString)) {
			return "";
		}
		Random random = new Random();
		String[] tokens = StringUtils.splitPreserveAllTokens(alphaString, split);
		int number = random.nextInt(tokens.length);
		return tokens[number];
	}
}
