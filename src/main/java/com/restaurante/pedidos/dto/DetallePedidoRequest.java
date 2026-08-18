package com.restaurante.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetallePedidoRequest {
    @NotNull
    private Long productoId;
    @NotNull
    @Min(1)
    private Integer cantidad;

    public Long getProductoId() { return productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
