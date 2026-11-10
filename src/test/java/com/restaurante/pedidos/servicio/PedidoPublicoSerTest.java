package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.dto.*;
import com.restaurante.pedidos.entidades.*;
import com.restaurante.pedidos.repositorio.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class PedidoPublicoSerTest {
    @Mock PedidoRepository pedidoRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock ProductoRepository productoRepository;
    @InjectMocks PedidoSer servicio;

    @Test
    void creaPedidoPublicoConCodigoAleatorioYTotalCalculadoEnServidor() {
        Cliente cliente = new Cliente();
        cliente.setId(7L);
        Producto producto = new Producto();
        producto.setId(3L);
        producto.setNombre("Lomo saltado");
        producto.setPrecio(new BigDecimal("32.00"));
        producto.setDisponible(true);

        when(clienteRepository.findByCorreoIgnoreCase("ana@example.com")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente guardado = inv.getArgument(0);
            guardado.setId(7L);
            return guardado;
        });
        when(productoRepository.findById(3L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientePedidoPublicoRequest datos = new ClientePedidoPublicoRequest();
        datos.setNombre("Ana");
        datos.setApellido("Pérez");
        datos.setCorreo("ana@example.com");
        datos.setTelefono("999123456");
        DetallePedidoRequest detalle = new DetallePedidoRequest();
        detalle.setProductoId(3L);
        detalle.setCantidad(2);
        PedidoPublicoRequest request = new PedidoPublicoRequest();
        request.setCliente(datos);
        request.setDetalles(List.of(detalle));

        PedidoPublicoResponse respuesta = servicio.crearPublico(request);

        assertNotNull(respuesta.codigoSeguimiento());
        assertEquals(36, respuesta.codigoSeguimiento().length());
        assertEquals(new BigDecimal("64.00"), respuesta.total());
        assertEquals(EstadoPedido.PENDIENTE, respuesta.estado());
        assertEquals("Lomo saltado", respuesta.detalles().get(0).nombre());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void seguimientoPublicoDevuelveSoloDatosBasicosDelPedido() {
        Cliente cliente = new Cliente();
        cliente.setId(7L);
        Producto producto = new Producto();
        producto.setNombre("Limonada");
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setCodigoSeguimiento("12345678-1234-1234-1234-123456789012");
        pedido.setTotal(new BigDecimal("8.00"));
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(1);
        pedido.getDetalles().add(detalle);
        when(pedidoRepository.findByCodigoSeguimiento(pedido.getCodigoSeguimiento()))
                .thenReturn(Optional.of(pedido));

        PedidoPublicoResponse respuesta = servicio.seguimientoPublico(pedido.getCodigoSeguimiento());

        assertEquals(pedido.getCodigoSeguimiento(), respuesta.codigoSeguimiento());
        assertEquals("Limonada", respuesta.detalles().get(0).nombre());
        assertEquals(1, respuesta.detalles().get(0).cantidad());
    }
}
