package com.restaurante.pedidos.servicio;

import org.springframework.transaction.annotation.Transactional;
import com.restaurante.pedidos.dto.DetallePedidoRequest;
import com.restaurante.pedidos.dto.PedidoRequest;
import com.restaurante.pedidos.entidades.*;
import com.restaurante.pedidos.repositorio.*;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoSer {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public PedidoSer(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                         ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

@Transactional(readOnly = true)
public List<Pedido> listar() {
    return pedidoRepository.findAll();
}

@Transactional(readOnly = true)
public Pedido obtener(Long id) {
    return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
}

    public Pedido crear(PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            if (!producto.isDisponible()) {
                throw new RuntimeException("El producto no está disponible: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            pedido.getDetalles().add(detalle);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    public Pedido cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = obtener(id);
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) { pedidoRepository.delete(obtener(id)); }
}
