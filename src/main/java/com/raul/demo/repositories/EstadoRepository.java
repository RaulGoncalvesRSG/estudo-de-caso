package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raul.demo.domain.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {

}
