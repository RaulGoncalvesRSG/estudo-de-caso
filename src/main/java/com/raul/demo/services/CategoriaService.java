package com.raul.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Categoria;
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
		findById(obj.getId());				//Verifica se exise obj com o ID
		return repository.save(obj);
	}

	public void delete(Integer id) {
		findById(id);	
		
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}
}
