package com.restaurante.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PedidoPublicoRequest {
    @NotNull
    @Valid
    private ClientePedidoPublicoRequest cliente;

    @NotEmpty
    @Valid
    private List<DetallePedidoRequest> detalles;

    public ClientePedidoPublicoRequest getCliente() { return cliente; }
    public List<DetallePedidoRequest> getDetalles() { return detalles; }
    public void setCliente(ClientePedidoPublicoRequest cliente) { this.cliente = cliente; }
    public void setDetalles(List<DetallePedidoRequest> detalles) { this.detalles = detalles; }
}
