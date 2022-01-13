package com.raul.demo.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raul.demo.domain.enums.Perfil;
import com.raul.demo.domain.enums.TipoCliente;

@Entity
public class Cliente extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String nome;
	
	@Column(unique=true)			//Deixa o campo como único
	private String email;
	private String cpfOuCnpj;
	private Integer tipo;			//Guarda o valor inteiro do tipo do cliente
	
	@JsonIgnore		    //Para n aparecer a senha no Json mesmo que esteja encodado com o BCrypt
	private String senha;
	
	//Td operação q modificar o cliente irá refletir em cascata nos endereços
	@OneToMany(mappedBy="cliente", cascade=CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<>();
	
	@ElementCollection								   //Cria uma tabela no BD como entidade fraca
	@CollectionTable(name="TELEFONE")
	private Set<String> telefones = new HashSet<>();   //N há repetição de telefones
	
	//EAGER garante q sempre q buscar um cliente do BD, automaticamente os perfis sejam buscados
	@ElementCollection(fetch=FetchType.EAGER)	
	@CollectionTable(name="PERFIS")
	private Set<Integer> perfis = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<>();
	
	private String imageUrl;
	
	public Cliente() {
		//Todos clientes do sistema automaticamente terão o perfil do tipo CLIENTE
		addPerfil(Perfil.CLIENTE);
	}

	//O tipo para lado externo da classe é TipoCliente e não um inteiro
	public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo, String senha) {
		super(id);
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfOuCnpj;
		this.tipo = (tipo == null)? null : tipo.getCod();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	//Converte o Set de inteiros para um Set<Perfil>
	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());			//Add apenas o número inteiro
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
