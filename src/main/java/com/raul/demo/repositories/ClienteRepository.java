package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raul.demo.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
