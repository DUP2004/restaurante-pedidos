package com.restaurante.pedidos.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.pedidos.entidades.Cliente;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
