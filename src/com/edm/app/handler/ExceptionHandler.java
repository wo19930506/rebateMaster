package com.edm.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class ExceptionHandler extends AbstractHandlerExceptionResolver {

	private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		String url = request.getRequestURL().toString();
		String param = request.getQueryString();
		if (param != null && !param.equals("")) {
			url = url + "?" + param;
		}
		
		logger.info("(ExceptionHandler) method: " + request.getMethod() + ", url: " + url + ", error: " + e);
		return new ModelAndView("common/404");
	}
}
