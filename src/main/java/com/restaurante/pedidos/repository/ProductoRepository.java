package com.restaurante.pedidos.repository;
import com.restaurante.pedidos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductoRepository extends JpaRepository<Producto, Long> {}
