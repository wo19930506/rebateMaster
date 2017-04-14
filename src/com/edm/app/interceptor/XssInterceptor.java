package com.edm.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.edm.modules.utils.Encodes;
import com.edm.utils.web.Clean;

public class XssInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String action = request.getRequestURI();
		String query = request.getQueryString();

        String[] links = { "/join_api" };
        for (String link : links) {
            if (StringUtils.startsWith(action, link)) {
                return true;
            }
        }
		
		if (!r(StringUtils.substringBefore(action, ";jsessionid=")) || !r(StringUtils.substringAfter(action, ";jsessionid="))) {
			response.sendRedirect("/404");
			return false;
		}
		if (StringUtils.isNotBlank(query)) {
		    boolean xss = false;
		    String parameters = "";
			for (String each : StringUtils.splitPreserveAllTokens(query, "&")) {
				each = Encodes.urlDecode(each);
				if (!r(StringUtils.substringBefore(each, "=")) || !Clean.clean(StringUtils.substringAfter(each, "="))) {
                    xss = true;
                    continue;
				}
				parameters += "&" + each;
			}
			
            if (xss) {
                if (StringUtils.isNotBlank(parameters) && StringUtils.startsWith(parameters, "&")) {
                    parameters = "?" + StringUtils.removeStart(parameters, "&");
                }
                response.sendRedirect(action + parameters);
                return false;
            }
		}
		
		return true;
	}
	
	private static boolean r(String str) {
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!(c >= 'A' && c <= 'Z') && 
				!(c >= 'a' && c <= 'z') && 
				!(c >= '0' && c <= '9') &&
				!(c == '/' || c == '_' || c == '.')) {
				return false;
			}
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		boolean a = r("asdaa34563496_./asc./asdasd_adaASFF");
		System.out.println(a);
	}

}
