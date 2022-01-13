package com.raul.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

//Classe para receber a entrada de dados para atualização de senha do usuário
public class SenhaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotEmpty(message="Preenchimento obrigatório")
	private String senhaAtual;
	
	@NotEmpty(message="Preenchimento obrigatório")
	private String novaSenha;
	
	public SenhaDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
}
