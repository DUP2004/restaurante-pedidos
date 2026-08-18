package com.restaurante.pedidos.repository;
import com.restaurante.pedidos.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
