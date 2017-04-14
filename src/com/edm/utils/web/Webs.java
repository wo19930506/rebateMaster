package com.edm.utils.web;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edm.modules.utils.Property;
import com.edm.utils.consts.Config;

public class Webs {
	
	private static final Logger logger = LoggerFactory.getLogger(Webs.class);

	public static final String WEB_ROOT = StringUtils.substringBefore(Webs.class.getResource("").getPath(), "/WEB-INF");
	public static final String ROOT_PATH = Property.getStr(Config.ROOT_PATH);
	public static final String FILE_SERVER = Property.getStr(Config.FILE_SERVER);
	
	public static String ip(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_CLIENT_IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();

		return ip;
	}

	public static String browser(HttpServletRequest request) {
		String browser = request.getHeader("USER-AGENT");
		if (StringUtils.contains(browser, "MSIE")) {
			browser = "IE" + StringUtils.substringAfter(browser, "MSIE ");
			browser = StringUtils.substringBefore(browser, ";");
		} else if (StringUtils.contains(browser, "Firefox")) {
			browser = "Firefox" + StringUtils.substringAfter(browser, "Firefox");
		} else if (StringUtils.contains(browser, "Chrome")) {
			browser = "Chrome" + StringUtils.substringAfter(browser, "Chrome");
			browser = StringUtils.substringBefore(browser, " ");
		} else if (StringUtils.contains(browser, "Opera")) {
			browser = StringUtils.replace(browser, "Opera ", "Opera/");
			browser = "Opera" + StringUtils.substringAfter(browser, "Opera");
			browser = StringUtils.substringBefore(browser, " ");
		} else if (StringUtils.contains(browser, "Safari")) {
			browser = "Safari" + StringUtils.substringAfter(browser, "Safari");
		} else if (StringUtils.contains(browser, "Navigator")) {
			browser = "Navigator" + StringUtils.substringAfter(browser, "Navigator");
		} else {
			browser = "Other";
		}

		return StringUtils.replace(browser, "/", " ");
	}

	public static String charset(String charset) {
		if (StringUtils.isNotBlank(charset)) {
			charset = StringUtils.replace(charset, "text/html", "");
			charset = StringUtils.replace(charset, ";", "");
			charset = StringUtils.replace(charset, " ", "");
			charset = StringUtils.replace(charset, "charset=", "");
		}
		if (StringUtils.isBlank(charset)) {
			charset = Charset.defaultCharset().name();
		}
		return charset;
	}
	
	public static final String rootPath() {
		return ROOT_PATH;
	}
	
	public static final String location(HttpServletRequest request) {
		String action = request.getRequestURI();
		String query = request.getQueryString();
		if (StringUtils.isNotBlank(query)) {
			StringBuffer sbff = new StringBuffer();
			for (String parameter : StringUtils.splitPreserveAllTokens(query, "&")) {
				String key = StringUtils.substringBefore(parameter, "=");
				String value = StringUtils.substringAfter(parameter, "=");

				if (StringUtils.equals(key, "pageNo") || StringUtils.equals(key, "pageSize") || StringUtils.equals(key, "_id")) {
					continue;
				}
				
				sbff.append(key).append("=").append(value).append("&");
			}

			query = sbff.toString();
			if (StringUtils.endsWith(query, "&")) {
				query = StringUtils.removeEnd(query, "&");
			}
		}
		
		return action + (StringUtils.isBlank(query) ? "" : "?" + query);
	}
	
	public static ServletOutputStream output(HttpServletResponse response, String fileName) {
		try {
			setHeader(response, fileName);
			return response.getOutputStream();
		} catch (IOException e) {
			logger.error("(Webs:out) error: ", e);
		}
		return null;
	}

	public static void setHeader(HttpServletResponse response, String fileName) {
		response.setCharacterEncoding("gbk");
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
	}
	
	public static final String webRoot() {
		return WEB_ROOT;
	}
	
	public static final String fileServer() {
		return FILE_SERVER;
	}
	
}
