package com.restaurante.pedidos.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.pedidos.entidades.Categoria;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
