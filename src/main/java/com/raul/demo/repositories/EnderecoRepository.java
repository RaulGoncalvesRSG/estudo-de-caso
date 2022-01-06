package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raul.demo.domain.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

}
