package com.restaurante.pedidos.controladores;

import com.restaurante.pedidos.entidades.Cliente;
import com.restaurante.pedidos.servicio.ClienteSer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteCont {
    private final ClienteSer service;
    public ClienteCont(ClienteSer service) { this.service = service; }

    @GetMapping public List<Cliente> listar() { return service.listar(); }
    @GetMapping("/{id}") public Cliente obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Cliente guardar(@Valid @RequestBody Cliente c) { return service.guardar(c); }
    @PutMapping("/{id}") public Cliente actualizar(@PathVariable Long id, @Valid @RequestBody Cliente c) { return service.actualizar(id, c); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
