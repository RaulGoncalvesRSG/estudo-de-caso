package com.raul.demo.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.raul.demo.services.DBService;
import com.raul.demo.services.SmtpEmailService;
import com.raul.demo.services.email.EmailService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	@Value("{spring.jpa.hibernate.ddl-auto}")		//Recupera o valor do arquivo .properties
	private String estrategia;			//Estragégia de geração do BD
	
	@Bean			//Método responsável por instanciar o BD no profile = dev
	public boolean instantiateDatabase() throws ParseException {
		/*Executa a instanciação do BD apenas se a estrategia for "create". Isso é para evitar
		q a instanciação e recriação do BD seja feita tds vezes q o programa for executado*/
		if (!"create".equals(estrategia)) {
			return false;
		}
		
		dbService.instantiateTestDatabase();
		return true;	//O método precisa retornar algo, n pode ser void
	}
	
	@Bean				
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
