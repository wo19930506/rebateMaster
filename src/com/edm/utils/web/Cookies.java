package com.edm.utils.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;

public class Cookies {

	public static final int MAX_AGE = 60 * 60 * 24 * 7; // 一周
	public static final String PATH = "/";
	public static final String USER_ID = "SID";
	public static final String PAGE_SIZE = "PSC";

	public static Cookie add(String key, String name) {
		Cookie cookie = new Cookie(key, name);
		cookie.setPath(PATH);
		cookie.setMaxAge(MAX_AGE);
		return cookie;
	}

	public static Cookie delete(String key) {
		Cookie cookie = new Cookie(key, "");
		cookie.setMaxAge(0);
		cookie.setPath(PATH);
		return cookie;
	}

	public static final String get(Cookie cookie, String key) {
		try {
			String name = cookie.getName();
			if (name != null && name.equals(key)) {
				return cookie.getValue();
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void add(HttpServletResponse response, String key, String name) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie cookie = new Cookie(key, name);
		cookie.setPath(PATH);
		cookie.setMaxAge(MAX_AGE);
		response.addCookie(cookie);
	}
	
	public static final String get(HttpServletRequest request, String name) {
		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie cookie = getCookieByName(request, name);
		if(cookie == null) return null;
		return cookie.getValue();
	}
	
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name){
	    Map<String, Cookie> cookieMap = readCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }
	}
	
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String, Cookie> readCookieMap(HttpServletRequest request){  
	    Map<String, Cookie> cookieMap = Maps.newHashMap();
	    Cookie[] cookies = request.getCookies();
	    if(null != cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
}
