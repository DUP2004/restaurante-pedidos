package com.restaurante.pedidos.dto;

import com.restaurante.pedidos.entidades.EstadoPedido;
import com.restaurante.pedidos.entidades.Pedido;
import java.math.BigDecimal;
import java.util.List;

public record PedidoPublicoResponse(
        Long id,
        String codigoSeguimiento,
        BigDecimal total,
        EstadoPedido estado,
        List<Detalle> detalles) {

    public record Detalle(String nombre, Integer cantidad) {}

    public static PedidoPublicoResponse desde(Pedido pedido) {
        List<Detalle> detalles = pedido.getDetalles().stream()
                .map(item -> new Detalle(item.getProducto().getNombre(), item.getCantidad()))
                .toList();
        return new PedidoPublicoResponse(
                pedido.getId(),
                pedido.getCodigoSeguimiento(),
                pedido.getTotal(),
                pedido.getEstado(),
                detalles
        );
    }
}
