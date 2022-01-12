package com.raul.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Cidade;
import com.raul.demo.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;

	public List<Cidade> findByEstado(Integer estadoId) {
		return repository.findCidades(estadoId);
	}
}
