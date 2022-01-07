package com.raul.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raul.demo.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
