package com.raul.demo.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.raul.demo.domain.enums.Perfil;

/*Classe de usuário q implementa a interface UserDetails. Esta interface é um contrato q o Spring
Security precisa para trabalhar com usuários. UserSS é um usuário q atende o contrato do Spring
Security (SS)*/
public class UserSS implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;		//Lista de perfis
	
	public UserSS() {
	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		//Converte o Set<Perfil> para uma coleção GrantedAuthority. Pega a descrição de cada perfil
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}

	public Integer getId() {
		return id;
	}
	
	@Override			//Retorna os perfis do usuário
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override		//Verifica se a conta n está expirada. Pode colocar uma lógoca de expiração
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override		//Verifica se a conta n está bloqueada
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override		//Verifica se as credenciais n estão expiradas
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override		//Verifica se o usuário está habilitado/ativo
	public boolean isEnabled() {
		return true;
	}
	
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}
}
