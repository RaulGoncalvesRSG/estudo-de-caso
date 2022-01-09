package com.raul.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raul.demo.domain.PagamentoComBoleto;
import com.raul.demo.domain.PagamentoComCartao;

/*Classe de configuração para o Jackson. Essa configuração foi preciso pq do
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
que fica na superclasse Pagamento*/
@Configuration
public class JacksonConfig {
	/*Link de um tópico q discute a criação dessa configuração:
	 https://stackoverflow.com/questions/41452598/overcome-can-not-construct-instance-ofinterfaceclass-without-hinting-the-pare*/
	/*Código padrão q é exigência da biblioteca Jackson. O que varia de projeto é a subclasse para 
	 ser registrada*/
	@Bean		
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				//Especifica a subclasse q será registrada
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
				super.configure(objectMapper);
			}
		};
		return builder;
	}
}
