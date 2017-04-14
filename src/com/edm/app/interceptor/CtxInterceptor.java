package com.edm.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.edm.modules.utils.Property;
import com.edm.utils.UserSession;
import com.edm.utils.View;
import com.edm.utils.consts.Config;
import com.edm.utils.web.XSS;

public class CtxInterceptor extends HandlerInterceptorAdapter {

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("ctx", request.getContextPath());
		request.setAttribute("currentUser", UserSession.getUser());
		request.setAttribute("xss", new XSS());
        request.setAttribute("view", new View());
        request.setAttribute("appUrl", Property.getStr(Config.APP_URL));
        request.setAttribute("website", Property.getStr(Config.WEBSITE_URL));
        String hrCatalogShow = StringUtils.isBlank(Property.getStr(Config.HR_CATALOG_SHOW)) ? "true" : Property.getStr(Config.HR_CATALOG_SHOW);
        request.setAttribute("hrCatalogShow", hrCatalogShow);
		return true;
	}
}