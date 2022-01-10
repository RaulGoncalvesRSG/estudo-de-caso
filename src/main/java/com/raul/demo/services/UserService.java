package com.raul.demo.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.raul.demo.security.UserSS;

public class UserService {
	
	//Retorna o usuário logado/autenticado no sistema. Retorna o usuário do Spring Securuty (UserSS)
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;			//Pode n haver usuário logado no sistema
		}
	}
}
