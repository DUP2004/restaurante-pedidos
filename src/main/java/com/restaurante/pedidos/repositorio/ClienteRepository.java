package com.restaurante.pedidos.repositorio;

import com.restaurante.pedidos.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreoIgnoreCase(String correo);
}
