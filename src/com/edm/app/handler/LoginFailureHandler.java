package com.edm.app.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.edm.modules.orm.MapBean;
import com.edm.modules.utils.mapper.ObjectMappers;
import com.edm.utils.consts.Sessions;
import com.edm.utils.except.AuthErrors;
import com.edm.utils.web.Views;

public class LoginFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(LoginFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		MapBean mb = new MapBean();
		if (exception instanceof AuthErrors) {
			Views.error(mb, exception.getMessage());
		} else if (exception instanceof BadCredentialsException) {
			Views.error(mb, "密码输入有误");
		} else {
			Views.error(mb, "登录失败");
			logger.error(exception.getMessage(), exception);
		}

		HttpSession session = request.getSession();
		int count = session.getAttribute(Sessions.KAPTCHA_COUNT) == null ? 0 : (Integer) session.getAttribute(Sessions.KAPTCHA_COUNT);
		
		mb.put("count", count);

		ObjectMappers.renderJson(response, mb);
	}
}
