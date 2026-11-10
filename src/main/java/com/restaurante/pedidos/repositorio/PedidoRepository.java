package com.restaurante.pedidos.repositorio;

import com.restaurante.pedidos.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCodigoSeguimiento(String codigoSeguimiento);
}
