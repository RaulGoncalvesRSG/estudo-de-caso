package com.raul.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Categoria;
import com.raul.demo.dto.CategoriaDTO;
import com.raul.demo.repositories.CategoriaRepository;
import com.raul.demo.services.exceptions.DataIntegrityException;
import com.raul.demo.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public Categoria findById(Integer id) {
		Optional<Categoria> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! ID: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
	public Categoria insert(Categoria obj) {
		return repository.save(obj);
	}

	public Categoria update(Categoria obj) {
		Categoria newObj = findById(obj.getId());		//newObj é monitorado pelo JPA
		updateData(newObj, obj);
		return repository.save(newObj);
	}

	public void delete(Integer id) {
		findById(id);	
		
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}
	
	//Contagem da página começa com 0
	public Page<Categoria> findPage(Integer page, Integer linhasPorPagina, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linhasPorPagina, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	//Instancia uma Categoria a partir do obj DTO
	public Categoria fromDTO(CategoriaDTO obDto) {
		return new Categoria(obDto.getId(), obDto.getNome());
	}
	
	//Atualiza o newObj com os novos dados vindo na requisição
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
