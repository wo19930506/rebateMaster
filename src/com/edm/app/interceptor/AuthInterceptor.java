package com.edm.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.edm.app.auth.Auth;
import com.edm.utils.web.Webs;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	static {
		Auth.setup();
		Auth.load(Webs.rootPath() + "/WEB-INF/classes/resources/LICENSE");
		Auth.robot(Webs.webRoot() + "/WEB-INF/classes/resources/ifcfg-eth0");
//		Auth.robot("/etc/sysconfig/network-scripts/ifcfg-eth0");  //LINUX系统下
//		Auth.sends();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String link = request.getServletPath();
		
		if (StringUtils.startsWith(link, "/403") || StringUtils.startsWith(link, "/404")) {
			return true;
		}
		
		if (!Auth.isSetup()) {
			logger.info("(Auth:handle) error: auth is off");
			response.sendRedirect("/403");
			return false;
		}
		
		String robot = Auth.ROBOT;
//		String code = Auth.MAP.get(UrlMap.CODE.getAction());
//		String code = Auth.MAP.get("LICENSE_PASSWD");

		if (!Auth.size()) {
			logger.info("(Auth:handle) error: LICENSE is error");
			response.sendRedirect("/403");
			return false;
		}
		if (StringUtils.isBlank(robot)) {
			logger.info("(Auth:handle) error: ROBOT is error");
			response.sendRedirect("/403");
			return false;
		}
		if (!Auth.key(robot)) {
			logger.info("Auth is fail ");
			logger.info("(Auth:handle) error: KEY is error");
			response.sendRedirect("/403");
			return false;
		}
//		if (!Auth.link(link, robot, code)) {
//			logger.info("(Auth:handle) error: " + link + " is forbid");
//			response.sendRedirect("/404");
//			return false;
//		}

		return true;
	}
}
