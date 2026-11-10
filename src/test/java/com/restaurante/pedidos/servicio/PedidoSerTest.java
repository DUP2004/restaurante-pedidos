package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.dto.DetallePedidoRequest;
import com.restaurante.pedidos.dto.PedidoRequest;
import com.restaurante.pedidos.entidades.*;
import com.restaurante.pedidos.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoSerTest {
    @Mock PedidoRepository pedidoRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock ProductoRepository productoRepository;
    @InjectMocks PedidoSer servicio;
    private Cliente cliente;
    private Producto producto;

    @BeforeEach void preparar() {
        cliente = new Cliente(); cliente.setId(1L); cliente.setNombre("Ana"); cliente.setApellido("Pérez");
        producto = new Producto(); producto.setId(10L); producto.setNombre("Lomo saltado");
        producto.setPrecio(new BigDecimal("32.00")); producto.setDisponible(true);
    }

    @Test void creaPedidoConVariosProductosYCalculaTotal() {
        Producto bebida = new Producto(); bebida.setId(11L); bebida.setNombre("Limonada");
        bebida.setPrecio(new BigDecimal("8.50")); bebida.setDisponible(true);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(productoRepository.findById(11L)).thenReturn(Optional.of(bebida));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoRequest req = new PedidoRequest(); req.setClienteId(1L);
        req.setDetalles(List.of(detalle(10L, 2), detalle(11L, 3)));
        Pedido resultado = servicio.crear(req);

        assertEquals(2, resultado.getDetalles().size());
        assertEquals(new BigDecimal("89.50"), resultado.getTotal());
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test void rechazaPedidoSiClienteNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        PedidoRequest req = new PedidoRequest(); req.setClienteId(99L); req.setDetalles(List.of(detalle(10L, 1)));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> servicio.crear(req));
        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test void rechazaProductoInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(404L)).thenReturn(Optional.empty());
        PedidoRequest req = new PedidoRequest(); req.setClienteId(1L); req.setDetalles(List.of(detalle(404L, 1)));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> servicio.crear(req));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test void rechazaProductoAgotado() {
        producto.setDisponible(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        PedidoRequest req = new PedidoRequest(); req.setClienteId(1L); req.setDetalles(List.of(detalle(10L, 1)));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> servicio.crear(req));
        assertTrue(ex.getMessage().contains("no está disponible"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test void cambiaEstadoDelPedidoExistente() {
        Pedido pedido = new Pedido(); pedido.setId(5L); pedido.setCliente(cliente);
        when(pedidoRepository.findById(5L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        Pedido resultado = servicio.cambiarEstado(5L, EstadoPedido.LISTO);
        assertEquals(EstadoPedido.LISTO, resultado.getEstado());
    }

    @Test void informaSiPedidoNoExiste() {
        when(pedidoRepository.findById(777L)).thenReturn(Optional.empty());
        assertEquals("Pedido no encontrado", assertThrows(RuntimeException.class, () -> servicio.obtener(777L)).getMessage());
    }

    private DetallePedidoRequest detalle(Long productoId, int cantidad) {
        DetallePedidoRequest d = new DetallePedidoRequest(); d.setProductoId(productoId); d.setCantidad(cantidad); return d;
    }
}
