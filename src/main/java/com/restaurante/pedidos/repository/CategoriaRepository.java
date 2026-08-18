package com.restaurante.pedidos.repository;
import com.restaurante.pedidos.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
