package com.raul.demo.services.email;

import org.springframework.mail.SimpleMailMessage;

import com.raul.demo.domain.Cliente;
import com.raul.demo.domain.Pedido;

public interface EmailService {

	//Email de confirmação de pedido
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	//Gera uma nova senha para um determinado cliente
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
