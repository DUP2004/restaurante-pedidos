package com.restaurante.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PedidoRequest {
    @NotNull
    private Long clienteId;

    @NotEmpty
    @Valid
    private List<DetallePedidoRequest> detalles;

    public Long getClienteId() { return clienteId; }
    public List<DetallePedidoRequest> getDetalles() { return detalles; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setDetalles(List<DetallePedidoRequest> detalles) { this.detalles = detalles; }
}
