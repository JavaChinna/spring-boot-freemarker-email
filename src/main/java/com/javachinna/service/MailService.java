package com.javachinna.service;

import com.javachinna.model.Mail;
import com.javachinna.model.User;

public interface MailService {

	void sendVerificationToken(String token, User user);

	void resetPasswordToken(final String token, final User user);

	void sendContactMail(Mail mailDTO);

}
