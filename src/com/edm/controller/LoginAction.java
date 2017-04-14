package com.edm.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.edm.app.auth.Auth;
import com.edm.modules.utils.Encodes;
import com.edm.modules.utils.Property;
import com.edm.utils.consts.Config;
import com.edm.utils.consts.Sessions;
import com.edm.utils.web.CSRF;
import com.edm.utils.web.Cookies;
import com.edm.utils.web.Views;

@Controller
public class LoginAction {

	@RequestMapping(value = "/sec", method = RequestMethod.GET)
	public String sec(HttpServletRequest request) {
		return Views.redirect("login");
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		if (!Auth.isSetup()) {
			return Views.redirect("403");
		}
		
		String token = CSRF.generate(request);
		CSRF.generate(request, "/forget", token);
		
		Object count = request.getSession().getAttribute(Sessions.KAPTCHA_COUNT);
		cookie(request, map);

		String website = Property.getStr(Config.WEBSITE_URL);

		if (StringUtils.isNotBlank(website)) {
			/*
			 * 暂时屏蔽JS SSO功能.
			String code = Websites.index(website);
			if (StringUtils.isNotBlank(code) && code.equals("HTTP/1.1 200 OK")) {
				map.put("website", website);
				map.put("hasWebsite", "true");
				if (UserSession.getUser() != null) {
					map.put("userId", Encodes.encodeBase64(UserSession.getUserId().getBytes()));
				}
			}
			*/
			map.put("website", website);
            map.put("hasWebsite", "false");
		}

		map.put("count", count == null ? 0 : (Integer) count);
		
		return "login";
	}

	private void cookie(HttpServletRequest request, ModelMap map) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return;
		}
		for (Cookie cookie : cookies) {
			String sid = Cookies.get(cookie, Cookies.USER_ID);
			if (sid != null) {
				map.put("sid", new String(Encodes.decodeBase64(sid)));
				map.put("remember_me", true);
				break;
			}
		}
	}
}
