package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raul.demo.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
