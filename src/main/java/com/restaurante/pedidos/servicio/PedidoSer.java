package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.dto.*;
import com.restaurante.pedidos.entidades.*;
import com.restaurante.pedidos.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    public List<Pedido> listar() { return pedidoRepository.findAll(); }

    @Transactional(readOnly = true)
    public Pedido obtener(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido crear(PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return crearConCliente(cliente, request.getDetalles());
    }

    @Transactional
    public PedidoPublicoResponse crearPublico(PedidoPublicoRequest request) {
        ClientePedidoPublicoRequest datos = request.getCliente();
        Cliente cliente = clienteRepository.findByCorreoIgnoreCase(datos.getCorreo().trim())
                .orElseGet(Cliente::new);
        cliente.setNombre(datos.getNombre().trim());
        cliente.setApellido(datos.getApellido().trim());
        cliente.setCorreo(datos.getCorreo().trim().toLowerCase());
        cliente.setTelefono(datos.getTelefono().trim());
        cliente = clienteRepository.save(cliente);

        Pedido pedido = crearConCliente(cliente, request.getDetalles());
        pedido.setCodigoSeguimiento(UUID.randomUUID().toString());
        pedido = pedidoRepository.save(pedido);
        return PedidoPublicoResponse.desde(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoPublicoResponse seguimientoPublico(String codigo) {
        Pedido pedido = pedidoRepository.findByCodigoSeguimiento(codigo)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return PedidoPublicoResponse.desde(pedido);
    }

    private Pedido crearConCliente(Cliente cliente, List<DetallePedidoRequest> detalles) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setCodigoSeguimiento(UUID.randomUUID().toString());
        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoRequest item : detalles) {
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

    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = obtener(id);
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminar(Long id) { pedidoRepository.delete(obtener(id)); }
}
