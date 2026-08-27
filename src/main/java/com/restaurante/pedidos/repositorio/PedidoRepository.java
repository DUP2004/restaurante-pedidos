package com.restaurante.pedidos.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.pedidos.entidades.Pedido;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
