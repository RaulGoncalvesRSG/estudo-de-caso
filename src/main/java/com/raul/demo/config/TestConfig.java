package com.raul.demo.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.raul.demo.services.DBService;

@Configuration
@Profile("test")  //Todos os @Bean da classe serão ativos apenas qnd spring.profiles.active=test
/*Classe de configuração específica para o profile "test". Isso é útil para as coisas especifcas 
 de cada profile*/
public class TestConfig {

	@Autowired
	private DBService dbService;
	
	@Bean			//Método responsável por instanciar o BD no profile = test
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;	//O método precisa retornar algo, n pode ser void
	}
}
