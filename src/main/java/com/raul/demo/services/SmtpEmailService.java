package com.raul.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.raul.demo.services.email.AbstractEmailService;

public class SmtpEmailService extends AbstractEmailService {

	/*MailSender é uma classe do Spring q qnd instanciar um obj dessa classe, ele pegará td os
	 dados de email estabelecidos no arquivo .properties e instanciar o obj com esses dados*/
	@Autowired
	private MailSender mailSender;
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email...");
		mailSender.send(msg);
		LOG.info("Email enviado");
	}
}
