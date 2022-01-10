package com.raul.demo.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component			//@Component para ser injetada em outras classes como componente
public class JWTUtil {
	
	@Value("${jwt.secret}")			//Valores do application.properties
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;
	
	//Passa um usuário e é gerado um token
	public String generateToken(String username) {
		//builder faz a geração do token 
		return Jwts.builder()
				.setSubject(username)
				//Horário atual do sistema + tempo de expiração
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				/*signWith - como vai assinar o token: determina o algoritmo e o qual é o segredo.
				 * getBytes pq o parâmetro pega um array de bytes*/
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	public boolean tokenValido(String token) {
		//Claims é um tipo do jwt q armazena as reivindicações do token
		Claims claims = getClaims(token);
		
		if (claims != null) {
			String username = claims.getSubject();				//getSubject retorna o usuário
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			
			//Verifica o instante atual é anterior à data de expiração
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	//Pega o usuário através do token
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
	
	private Claims getClaims(String token) {
		try {
			//Função q recupera os clains a partir de um token
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}
		catch (Exception e) {
			return null;			//token inválido
		}
	}
}
