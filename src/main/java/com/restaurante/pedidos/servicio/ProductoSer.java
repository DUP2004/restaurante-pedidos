package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.entidades.Categoria;
import com.restaurante.pedidos.entidades.Producto;
import com.restaurante.pedidos.repositorio.CategoriaRepository;
import com.restaurante.pedidos.repositorio.ProductoRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoSer {
    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoSer(ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar() { return repository.findAll(); }
    public Producto obtener(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado")); }

    public Producto guardar(Producto producto) {
        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);
        return repository.save(producto);
    }

    public Producto actualizar(Long id, Producto data) {
        Producto p = obtener(id);
        p.setNombre(data.getNombre());
        p.setDescripcion(data.getDescripcion());
        p.setPrecio(data.getPrecio());
        p.setDisponible(data.isDisponible());
        Categoria categoria = categoriaRepository.findById(data.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        p.setCategoria(categoria);
        return repository.save(p);
    }

    public void eliminar(Long id) { repository.delete(obtener(id)); }
}
