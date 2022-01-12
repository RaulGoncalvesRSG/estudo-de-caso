package com.raul.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.raul.demo.domain.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {

	@Transactional(readOnly = true)
	public List<Estado> findAllByOrderByNome();			//Retorna tds estados ordenado por nome
}
