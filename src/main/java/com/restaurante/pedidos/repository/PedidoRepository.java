package com.restaurante.pedidos.repository;
import com.restaurante.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
