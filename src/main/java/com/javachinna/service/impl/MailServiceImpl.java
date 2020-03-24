package com.javachinna.service.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.javachinna.model.Mail;
import com.javachinna.model.User;
import com.javachinna.service.MailService;
import com.javachinna.service.MessageService;

import freemarker.template.Configuration;

/**
 * @author Chinna
 *
 */
@Service
public class MailServiceImpl implements MailService {

	private final Logger logger = LogManager.getLogger(getClass());
	public final static String BASE_URL = "baseUrl";
	public static final String LINE_BREAK = "<br>";

	@Value("${base-url}")
	private String baseUrl;

	@Value("${support.email}")
	private String supportEmail;

	@Autowired
	private MessageService messageService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	Configuration freemarkerConfiguration;

	@Override
	public void sendVerificationToken(String token, User user) {
		final String confirmationUrl = baseUrl + "/registrationConfirm?token=" + token;
		final String message = messageService.getMessage("message.verificationMail");
		sendHtmlEmail(messageService.getMessage("message.verification"), message + LINE_BREAK + confirmationUrl, user);
	}

	@Override
	public void resetPasswordToken(String token, User user) {
		final String url = baseUrl + "/resetPassword?id=" + user.getId() + "&token=" + token;
		final String message = messageService.getMessage("message.resetPasswordEmail");
		sendHtmlEmail(messageService.getMessage("message.resetPassword"), message + LINE_BREAK + url, user);
	}

	private String geFreeMarkerTemplateContent(Map<String, Object> model, String templateName) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), model));
			return content.toString();
		} catch (Exception e) {
			System.out.println("Exception occured while processing fmtemplate:" + e.getMessage());
		}
		return "";
	}

	@Override
	public void sendContactMail(Mail mailDTO) {
		String subject = MessageFormat.format(messageService.getMessage("mail.contact.subject"), (mailDTO.getFromName() + " [ " + mailDTO.getFrom() + " ] "));
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "New Contact Email"); // so that we can reference it from HTML
		model.put("message", mailDTO);
		model.put("greeting", messageService.getMessage("mail.contact.greeting"));
		model.put(BASE_URL, baseUrl);
		try {
			sendHtmlMail(mailDTO.getFrom(), supportEmail, subject, geFreeMarkerTemplateContent(model, "mail/contact.ftl"), true);
			logger.info(String.format("Contact Email sent from: %s", mailDTO.getFrom()));
		} catch (MessagingException e) {
			logger.error("Failed to send contact mail", e);
		}
	}

	private void sendHtmlEmail(String subject, String msg, User user) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", user.getDisplayName());
		model.put("msg", msg);
		model.put("title", subject);
		model.put(BASE_URL, baseUrl);
		try {
			sendHtmlMail(supportEmail, user.getEmail(), subject, geFreeMarkerTemplateContent(model, "mail/verification.ftl"), false);
		} catch (MessagingException e) {
			logger.error("Failed to send mail", e);
		}
	}

	private void sendHtmlMail(String from, String to, String subject, String body, boolean attachImage) throws MessagingException {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");

		helper.setFrom(from);
		if (to.contains(",")) {
			helper.setTo(to.split(","));
		} else {
			helper.setTo(to);
		}
		helper.setSubject(subject);
		helper.setText(body, true);
		if (attachImage) {
			// Inline image
			helper.addInline("logo.png", new ClassPathResource("javachinna-logo.png"));
			// attachment
			helper.addAttachment("javachinna.jpg", new ClassPathResource("javachinna.jpg"));
		}

		mailSender.send(mail);
		logger.info("Sent mail: {0}", subject);
	}
}
