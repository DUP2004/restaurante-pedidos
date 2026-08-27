package com.restaurante.pedidos.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.pedidos.entidades.Producto;
public interface ProductoRepository extends JpaRepository<Producto, Long> {}
