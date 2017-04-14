package com.edm.app.jms;

import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.edm.modules.utils.Property;
import com.edm.modules.utils.spring.CtxHolder;
import com.edm.modules.utils.template.Velocitys;
import com.edm.utils.consts.Config;
import com.edm.utils.consts.Mail;
import com.edm.utils.file.Files;
import com.edm.utils.file.Suffix;
import com.edm.utils.web.Webs;

/**
 * MIME邮件服务类.
 * 
 * 由Velocity引擎生成的的html格式邮件, 并带有附件.
 */
public class MimeMailService {

	private static final String DEFAULT_ENCODING = "utf-8";

	private static Logger logger = LoggerFactory.getLogger(MimeMailService.class);

	private VelocityEngine velocityEngine;

	/**
	 * 发送MIME格式的用户保单通知邮件.
	 */
	public void sendNotificationMail(Map<String, Object> map) {
		try {
			JavaMailSenderImpl mailSenderImpl = CtxHolder.getBean("mailSender");
			mailSenderImpl.setHost(Property.getStr(Config.SMTP_HOST));
			mailSenderImpl.setPort(Property.getInt(Config.SMTP_PORT));
			JavaMailSender mailSender = mailSenderImpl;
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, false, DEFAULT_ENCODING);

			helper.setFrom((String) map.get(Mail.FROM));
			helper.setTo((String) map.get(Mail.TO));
			helper.setSubject((String) map.get(Mail.SUBJECT));
			helper.setText(generateContent(map), true);
			
			mailSender.send(msg);
			logger.info("HTML版邮件已发送至" + (String) map.get(Mail.TO));
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	/**
	 * 生成html格式内容.
	 */
	private String generateContent(Map<String, Object> map) throws MessagingException {
		String templateFileName = (String) map.get(Mail.TEMPLATE_FILE_NAME);
		String suffix = Files.suffix(templateFileName);
        if (suffix.endsWith(Suffix.TXT)) {
            String content = Files.get(Webs.rootPath(), templateFileName);
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                content = StringUtils.replace(content, "$" + key + "$", value);
            }
            return content;
        } else {
            return Velocitys.renderFile(templateFileName, velocityEngine, DEFAULT_ENCODING, map);
        }
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}
