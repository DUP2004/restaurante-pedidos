package com.restaurante.pedidos.controladores;

import com.restaurante.pedidos.entidades.Producto;
import com.restaurante.pedidos.servicio.ProductoSer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoCont {
    private final ProductoSer service;
    public ProductoCont(ProductoSer service) { this.service = service; }

    @GetMapping public List<Producto> listar() { return service.listar(); }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Producto guardar(@Valid @RequestBody Producto p) { return service.guardar(p); }
    @PutMapping("/{id}") public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto p) { return service.actualizar(id, p); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
