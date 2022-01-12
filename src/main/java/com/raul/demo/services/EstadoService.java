package com.raul.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raul.demo.domain.Estado;
import com.raul.demo.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;
	
	public List<Estado> findAll() {
		return repository.findAllByOrderByNome();
	}
}
