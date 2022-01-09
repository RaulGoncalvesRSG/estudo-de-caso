package com.raul.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Categoria;
import com.raul.demo.domain.Produto;
import com.raul.demo.repositories.CategoriaRepository;
import com.raul.demo.repositories.ProdutoRepository;
import com.raul.demo.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto findById(Integer id) {
		Optional<Produto> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! ID: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	public List<Produto> findAll() {
		return repository.findAll();
	}

	//Pesquisa por um nome e por uma lista de parâmetros (IDs dos produtos)
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		//Instancia a lista a partir da lista de IDs
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		
		return repository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);	
	}
}
