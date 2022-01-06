package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raul.demo.domain.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

}
