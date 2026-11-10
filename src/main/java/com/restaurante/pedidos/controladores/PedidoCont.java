package com.restaurante.pedidos.controladores;

import com.restaurante.pedidos.dto.PedidoPublicoRequest;
import com.restaurante.pedidos.dto.PedidoPublicoResponse;
import com.restaurante.pedidos.dto.PedidoRequest;
import com.restaurante.pedidos.entidades.EstadoPedido;
import com.restaurante.pedidos.entidades.Pedido;
import com.restaurante.pedidos.servicio.PedidoSer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
public class PedidoCont {
    private final PedidoSer service;
    public PedidoCont(PedidoSer service) { this.service = service; }

    @GetMapping public List<Pedido> listar() { return service.listar(); }
    @GetMapping("/{id}") public Pedido obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(@Valid @RequestBody PedidoRequest request) { return service.crear(request); }

    @PostMapping("/publicos")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoPublicoResponse crearPublico(@Valid @RequestBody PedidoPublicoRequest request) {
        return service.crearPublico(request);
    }

    @GetMapping("/seguimiento/{codigo}")
    public PedidoPublicoResponse seguimientoPublico(@PathVariable String codigo) {
        return service.seguimientoPublico(codigo);
    }

    @PutMapping("/{id}/estado")
    public Pedido cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        return service.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
