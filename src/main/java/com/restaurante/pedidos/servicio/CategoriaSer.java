package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.entidades.Categoria;
import com.restaurante.pedidos.repositorio.CategoriaRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaSer {
    private final CategoriaRepository repository;
    public CategoriaSer(CategoriaRepository repository) { this.repository = repository; }

    public List<Categoria> listar() { return repository.findAll(); }
    public Categoria obtener(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada")); }
    public Categoria guardar(Categoria categoria) { return repository.save(categoria); }
    public Categoria actualizar(Long id, Categoria data) {
        Categoria c = obtener(id);
        c.setNombre(data.getNombre());
        c.setDescripcion(data.getDescripcion());
        return repository.save(c);
    }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
}
