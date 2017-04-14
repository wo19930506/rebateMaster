package com.edm.utils.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edm.modules.utils.Property;
import com.edm.modules.utils.web.Randoms;
import com.edm.utils.Converts;
import com.edm.utils.consts.Config;
import com.edm.utils.consts.Value;
import com.edm.utils.except.Errors;

public class CSRF {

    private static final Logger logger = LoggerFactory.getLogger(CSRF.class);
    
	private static final String TOKEN = "token";
	private static final String ERROR = "Sorry，您访问的页面已过期，请刷新重试！";
	
	public static String generate(HttpServletRequest request) {
		referer(request);

		String link = request.getServletPath();
		String mapper = mapper(link);
		String code = Randoms.getRandomString(32);

		request.getSession().setAttribute(TOKEN + "_" + mapper, code);
		request.setAttribute(TOKEN, code);

		return code;
	}

	public static void generate(HttpServletRequest request, String link, String token) {
		String mapper = mapper(link);
		request.getSession().setAttribute(TOKEN + "_" + mapper, token);
	}
	
	public static void validate(HttpServletRequest request) {
		referer(request);

		String link = request.getServletPath();
		String mapper = mapper(link);
		
		String header = (String) Validator.validate(request.getHeader(TOKEN), R.CLEAN, R.REQUIRED, R.LENGTH, "{1,32}");
		String client = Https.getStr(request, "token", R.CLEAN, R.REQUIRED, R.LENGTH, "{1,32}");
		String server = (String) request.getSession().getAttribute(TOKEN + "_" + mapper);

		if (StringUtils.isBlank(header) || StringUtils.isBlank(client)
				|| !StringUtils.equals(header, server) || !StringUtils.equals(client, server)) {
			throw new Errors(ERROR);
		}
	}
	
	public static void auth(HttpServletRequest request) {
		referer(request);

		String link = request.getServletPath();
		String mapper = mapper(link);
		
		String client = Https.getStr(request, "token", R.CLEAN, R.REQUIRED, R.LENGTH, "{1,32}");
		String server = (String) request.getSession().getAttribute(TOKEN + "_" + mapper);

		if (StringUtils.isBlank(client) || !StringUtils.equals(client, server)) {
			throw new Errors(ERROR);
		}
	}
	
	private static String mapper(String link) {
		String[] split = StringUtils.split(link, "/");

		String mapper = Value.S;

		if (split.length > 2) {
			String module = split[0];
			String action = split[1];
			String m = split[2];
			mapper = module + "_" + action + "_" + m;
		}
		if (split.length == 1) {
			String m = split[0];
			if (m.equals("j_spring_security_check")) {
				m = "login";
			}
			
			mapper = m;
		}

		return mapper;
	}

	private static void referer(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		if (referer != null) {
			String[] appUrls = Converts._toStrings(Property.getStr(Config.APP_URLS));
            if (appUrls == null) {
                return;
            }
			
			boolean ok = false;
			for (String appUrl : appUrls) {
				if (StringUtils.startsWith(referer, appUrl)) {
					ok = true;
					break;
				}
			}

			if (!ok) {
                logger.error("(CSRF:referer) url: " + referer);
				throw new Errors("请求来源不是本网站");
			}
		}
	}
	
}
