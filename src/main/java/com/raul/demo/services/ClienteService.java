package com.raul.demo.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.raul.demo.domain.Cidade;
import com.raul.demo.domain.Cliente;
import com.raul.demo.domain.Endereco;
import com.raul.demo.domain.enums.Perfil;
import com.raul.demo.domain.enums.TipoCliente;
import com.raul.demo.dto.ClienteDTO;
import com.raul.demo.dto.ClienteNewDTO;
import com.raul.demo.repositories.ClienteRepository;
import com.raul.demo.repositories.EnderecoRepository;
import com.raul.demo.security.UserSS;
import com.raul.demo.services.exceptions.AuthorizationException;
import com.raul.demo.services.exceptions.DataIntegrityException;
import com.raul.demo.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
		
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	public Cliente findById(Integer id) {
		/*Verifica se o usuário buscado n possui o perfil ADMIN e se o ID do user pesquisado é 
		 * diferente do ID do usuário logado*/
		UserSS user = UserService.authenticated();
		
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! ID: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}
	
	//@Transactional para garantir que vai salvar tanto o cliente quanto o endereço na mesma transação do BD
	@Transactional	
	public Cliente insert(Cliente obj) {
		obj =repository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateData(newObj, obj);
		return repository.save(newObj);
	}

	public void delete(Integer id) {
		findById(id);	
		
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	}
	
	//O usuário consegue apenas pegar os dados do seu próprio email
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();			//Pega o usuário autenticado
		
		//Deu problema de autenticação ou o email n é o mesmo do usuário logado
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
	
		Cliente obj = repository.findByEmail(email);
		
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linhasPorPagina, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linhasPorPagina, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO obDto) {
		return new Cliente(obDto.getId(), obDto.getNome(), obDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), encoder.encode(objDto.getSenha()));
		Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDto.getTelefone1());
		
		if (objDto.getTelefone2() != null) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	//Faz o upload da foto de perfil do usuário
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		
		if (user == null) {				 
			throw new AuthorizationException("Acesso negado");
		}
		saveImageUrl(user, multipartFile);
		
		//Pega a img JPG
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);		//Recorta a img de forma quadrada
		jpgImage = imageService.resize(jpgImage, size);		//Redimensiona a img
		
		//Define o nome do arquivo q será enviado para o S3. Ex: cp1.jpg
		String fileName = prefix + user.getId() + ".jpg";
		
		//Repassa a chamada do método para o S3Service
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
	
	//Salva a URL da foto no Cliente no BD
	private void saveImageUrl(UserSS user, MultipartFile multipartFile) {
		URI uri = s3Service.uploadFile(multipartFile);			//Url da img salva na amazon
		Cliente cliente = repository.findById(user.getId()).get();
		cliente.setImageUrl(uri.toString());
		repository.save(cliente);				//Salva no BD o cliente com a foto atualizada
	}
}
