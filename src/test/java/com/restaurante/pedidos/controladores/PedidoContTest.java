package com.restaurante.pedidos.controladores;

import com.restaurante.pedidos.dto.PedidoPublicoRequest;
import com.restaurante.pedidos.dto.PedidoPublicoResponse;
import com.restaurante.pedidos.dto.PedidoRequest;
import com.restaurante.pedidos.entidades.EstadoPedido;
import com.restaurante.pedidos.entidades.Pedido;
import com.restaurante.pedidos.servicio.PedidoSer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoContTest {
    @Mock PedidoSer service;
    @InjectMocks PedidoCont controller;

    @Test void listaPedidos() {
        when(service.listar()).thenReturn(List.of(new Pedido()));
        assertEquals(1, controller.listar().size());
    }

    @Test void obtienePedido() {
        Pedido pedido = new Pedido();
        when(service.obtener(1L)).thenReturn(pedido);
        assertSame(pedido, controller.obtener(1L));
    }

    @Test void creaPedidoAdministrativo() {
        PedidoRequest request = new PedidoRequest();
        Pedido pedido = new Pedido();
        when(service.crear(request)).thenReturn(pedido);
        assertSame(pedido, controller.crear(request));
    }

    @Test void creaPedidoPublico() {
        PedidoPublicoRequest request = new PedidoPublicoRequest();
        PedidoPublicoResponse respuesta = new PedidoPublicoResponse(1L, "codigo", null, EstadoPedido.PENDIENTE, List.of());
        when(service.crearPublico(request)).thenReturn(respuesta);
        assertSame(respuesta, controller.crearPublico(request));
    }

    @Test void consultaSeguimientoPublico() {
        PedidoPublicoResponse respuesta = new PedidoPublicoResponse(1L, "codigo", null, EstadoPedido.LISTO, List.of());
        when(service.seguimientoPublico("codigo")).thenReturn(respuesta);
        assertSame(respuesta, controller.seguimientoPublico("codigo"));
    }

    @Test void actualizaEstado() {
        Pedido pedido = new Pedido();
        when(service.cambiarEstado(9L, EstadoPedido.LISTO)).thenReturn(pedido);
        assertSame(pedido, controller.cambiarEstado(9L, EstadoPedido.LISTO));
    }

    @Test void eliminaPedido() {
        assertDoesNotThrow(() -> controller.eliminar(10L));
        verify(service).eliminar(10L);
    }
}
