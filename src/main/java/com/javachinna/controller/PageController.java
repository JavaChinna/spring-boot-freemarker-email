package com.javachinna.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.javachinna.model.Mail;
import com.javachinna.model.User;
import com.javachinna.service.MailService;

@Controller
public class PageController {

	@Autowired
	MailService mailService;

	@GetMapping("/")
	public String mail() {
		return "home";
	}

	@GetMapping("/send")
	public String send() {
		User user = new User(1L, "Chinna", "t.chinna44@gmail.com");

		// Sending verification mail
		mailService.sendVerificationToken(UUID.randomUUID().toString(), user);

		// Sending password reset mail
		mailService.resetPasswordToken(UUID.randomUUID().toString(), user);

		// Sending contact mail
		Mail mail = new Mail();
		mail.setFromName(user.getDisplayName());
		mail.setFrom(user.getEmail());
		mail.setSubject("Spring Boot - Email with FreeMarker template");
		mail.setBody("Message body goes here");
		mailService.sendContactMail(mail);
		System.out.println("Done!");
		return "mail";
	}
}