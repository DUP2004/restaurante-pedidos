package com.restaurante.pedidos.controller;

import com.restaurante.pedidos.entity.Categoria;
import com.restaurante.pedidos.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }

    @GetMapping public List<Categoria> listar() { return service.listar(); }
    @GetMapping("/{id}") public Categoria obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Categoria guardar(@Valid @RequestBody Categoria c) { return service.guardar(c); }
    @PutMapping("/{id}") public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody Categoria c) { return service.actualizar(id, c); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
