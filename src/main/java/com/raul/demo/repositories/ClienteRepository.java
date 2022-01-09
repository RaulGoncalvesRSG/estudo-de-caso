package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.raul.demo.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	@Transactional(readOnly = true)		//Diminui o lock do gerenciamento de transação do BD
	Cliente findByEmail(String email);
}
