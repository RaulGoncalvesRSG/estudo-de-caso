package com.raul.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Cliente;
import com.raul.demo.repositories.ClienteRepository;
import com.raul.demo.security.UserSS;

/*Classe de implementação do UserDetailsService. UserDetailsService é uma interface q permite a 
busca pelo username (email) do usuário*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cliente = repository.findByEmail(email);
		
		if (cliente == null) {
			//Exceção do Spring Security já que está dentro do contexo de segurança
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(cliente.getId(), cliente.getEmail(), cliente.getSenha(), cliente.getPerfis());
	}
}
