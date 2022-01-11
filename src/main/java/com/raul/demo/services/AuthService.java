package com.raul.demo.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Cliente;
import com.raul.demo.repositories.ClienteRepository;
import com.raul.demo.services.email.EmailService;
import com.raul.demo.services.exceptions.ObjectNotFoundException;

//Classe responsávei por gerar uma nova senha para o usuário
@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(encoder.encode(newPass));	  //Seta a nova senha aleatória de forma encodada
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	//Gera uma senha aleatória que pode conter dígitos ou letras
	private String newPassword() {
		char[] vet = new char[10];
		
		for (int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	//https://unicode-table.com/en/
	//Retorna um caractere aleatório que pode ser dígito ou letra
	private char randomChar() {
		//Escolhe de forma aleatória se o caractere vai ser um dígito, letra min ou letra maiúscula
		int opt = rand.nextInt(3);
		
		if (opt == 0) { 		//gera um digito
			return (char) (rand.nextInt(10) + 48);			//10 dígitos possíveis (0-9)
		}
		else if (opt == 1) { 	//gera letra maiúscula
			return (char) (rand.nextInt(26) + 65);			//26 letras possíveis (a-z)
		}
		else { 					//gera letra minúscula
			return (char) (rand.nextInt(26) + 97);			//26 letras possíveis (A-Z)
		}
	}
}
