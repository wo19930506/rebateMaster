package com.edm.app.jms;

import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.edm.utils.consts.Mail;
import com.google.common.collect.Maps;

/**
 * 消息的异步被动接收者.
 * 
 * 使用Spring的MessageListenerContainer侦听消息并调用本Listener进行处理.
 */
public class NotifyMessageListener implements MessageListener {

	private static Logger logger = LoggerFactory.getLogger(NotifyMessageListener.class);

	@Autowired(required = false)
	private MimeMailService mimeMailService;
	
	/**
	 * MessageListener回调函数.
	 */
	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
			String templateFileName = mapMessage.getString(Mail.TEMPLATE_FILE_NAME);
			String from = mapMessage.getString(Mail.FROM);
			String to = mapMessage.getString(Mail.TO);
			String subject = mapMessage.getString(Mail.SUBJECT);
			String mtarget = mapMessage.getString(Mail.APP_URL);
			String userName = mapMessage.getString(Mail.USER_NAME);
			String email = mapMessage.getString(Mail.EMAIL);
			String randCode = mapMessage.getString(Mail.RAND_CODE);
			String registTime = mapMessage.getString(Mail.REGIST_TIME);
			
			// 打印消息详情
			logger.info("FROM: " + from + ", TO: " + to + ", SUBJECT: " + subject);

			// 发送邮件
			if (mimeMailService != null) {
				Map<String, Object> map = Maps.newHashMap();
				map.put(Mail.TEMPLATE_FILE_NAME, templateFileName);
				map.put(Mail.FROM, from);
				map.put(Mail.TO, to);
				map.put(Mail.SUBJECT, subject);
				map.put(Mail.APP_URL, mtarget);
				map.put(Mail.USER_NAME, userName);
				map.put(Mail.EMAIL, email);
				map.put(Mail.RAND_CODE, randCode);
				map.put(Mail.REGIST_TIME, registTime);
				
				mimeMailService.sendNotificationMail(map);
			}
		} catch (Exception e) {
			logger.error("处理消息时发生异常.", e);
		}
	}
}
