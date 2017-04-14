package com.edm.app.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.edm.app.security.Securitys;
import com.edm.modules.orm.MapBean;
import com.edm.modules.utils.Encodes;
import com.edm.modules.utils.mapper.ObjectMappers;
import com.edm.utils.UserSession;
import com.edm.utils.consts.Const;
import com.edm.utils.consts.Sessions;
import com.edm.utils.consts.Value;
import com.edm.utils.web.Cookies;
import com.edm.utils.web.Https;
import com.edm.utils.web.R;
import com.edm.utils.web.Views;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		MapBean mb = new MapBean();
		try {
			String rememberMe = Https.getStr(request, "remember_me", R.CLEAN, R.REGEX, "regex:^true|false$"); 
			if (StringUtils.isNotBlank(rememberMe) && StringUtils.equals(rememberMe, "true")) {
				response.addCookie(Cookies.add(Cookies.USER_ID, Encodes.encodeBase64(UserSession.getUserId().getBytes())));
			} else {
				response.addCookie(Cookies.delete(Cookies.USER_ID));
			}
			
			response.addCookie(Cookies.add(Cookies.PAGE_SIZE, String.valueOf(Const.PAGE_SIZE)));

			request.getSession().removeAttribute(Sessions.KAPTCHA_COUNT);
			
			String userIp = Securitys.getCurrentUserIp();
/*			Ip ip = ipService.get(userIp);
			String region = (ip != null ? ip.getRegion() : Value.S);
			
			historyService.save(UserSession.getCorpId(), UserSession.getUserId(), history(Webs.browser(request), userIp, region));*/	      
        	Views.ok(mb,"登录成功");
		} catch (Exception e) {
			Views.error(mb, "登录超时");
			logger.error("(Success:on) error: ", e);
		}
		
		mb.put("count", Value.I);
		
		ObjectMappers.renderJson(response, mb);
	}

/*	private History history(String client, String ip, String region) {
		History history = new History();
		history.setCorpId(UserSession.getCorpId());
		history.setUserId(UserSession.getUserId());
		history.setLoginIp(ip);
		history.setRegion(region);
		history.setClient(client);
		history.setResult(Value.T);
		return history;
	}*/
}
