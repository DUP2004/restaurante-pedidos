package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.entidades.Categoria;
import com.restaurante.pedidos.entidades.Producto;
import com.restaurante.pedidos.repositorio.CategoriaRepository;
import com.restaurante.pedidos.repositorio.ProductoRepository;
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
class ProductoSerTest {
    @Mock ProductoRepository repository;
    @Mock CategoriaRepository categoriaRepository;
    @InjectMocks ProductoSer servicio;

    @Test void listaProductos() {
        when(repository.findAll()).thenReturn(List.of(producto(1L, "Lomo", "25.00", categoria(1L))));
        assertEquals(1, servicio.listar().size());
    }

    @Test void obtieneProductoExistente() {
        Producto producto = producto(1L, "Lomo", "25.00", categoria(1L));
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        assertSame(producto, servicio.obtener(1L));
    }

    @Test void informaCuandoProductoNoExiste() {
        when(repository.findById(404L)).thenReturn(Optional.empty());
        RuntimeException error = assertThrows(RuntimeException.class, () -> servicio.obtener(404L));
        assertEquals("Producto no encontrado", error.getMessage());
    }

    @Test void guardaProductoConCategoriaExistente() {
        Categoria categoria = categoria(2L);
        Producto producto = producto(null, "Pizza", "30.00", categoria);
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(repository.save(producto)).thenReturn(producto);

        assertSame(producto, servicio.guardar(producto));
        assertSame(categoria, producto.getCategoria());
        verify(repository).save(producto);
    }

    @Test void rechazaGuardarProductoConCategoriaInexistente() {
        Producto producto = producto(null, "Pizza", "30.00", categoria(999L));
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class, () -> servicio.guardar(producto));

        assertEquals("Categoría no encontrada", error.getMessage());
        verify(repository, never()).save(any());
    }

    @Test void actualizaDatosDelProducto() {
        Categoria categoria = categoria(3L);
        Producto existente = producto(7L, "Hamburguesa", "20.00", categoria);
        Producto cambios = producto(null, "Hamburguesa doble", "28.00", categoria);
        cambios.setDescripcion("Con queso");
        cambios.setDisponible(false);
        when(repository.findById(7L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(categoria));
        when(repository.save(existente)).thenReturn(existente);

        Producto resultado = servicio.actualizar(7L, cambios);

        assertEquals("Hamburguesa doble", resultado.getNombre());
        assertEquals("Con queso", resultado.getDescripcion());
        assertEquals(new BigDecimal("28.00"), resultado.getPrecio());
        assertFalse(resultado.isDisponible());
        verify(repository).save(existente);
    }

    @Test void noActualizaProductoInexistente() {
        when(repository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> servicio.actualizar(8L, producto(null, "X", "1.00", categoria(1L))));
        verify(repository, never()).save(any());
    }

    @Test void noActualizaSiLaNuevaCategoriaNoExiste() {
        Categoria categoria = categoria(77L);
        Producto existente = producto(9L, "Torta", "10.00", categoria(1L));
        when(repository.findById(9L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> servicio.actualizar(9L, producto(null, "Torta", "12.00", categoria)));
        verify(repository, never()).save(any());
    }

    @Test void guardaImagenPngValida() {
        byte[] png = {(byte)0x89,0x50,0x4e,0x47,0x0d,0x0a,0x1a,0x0a};
        Producto producto = producto(10L, "Ensalada", "15.00", categoria(1L));
        when(repository.findById(10L)).thenReturn(Optional.of(producto));
        when(repository.save(producto)).thenReturn(producto);

        servicio.guardarImagen(10L, png, "image/png");

        assertArrayEquals(png, producto.getImagenDatos());
        assertEquals("image/png", producto.getImagenTipo());
        verify(repository).save(producto);
    }

    @Test void rechazaImagenInvalidaSinGuardar() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.guardarImagen(10L, new byte[]{1,2,3}, "image/png"));
        verify(repository, never()).save(any());
        verify(repository, never()).findById(any());
    }

    @Test void eliminaImagenDelProducto() {
        Producto producto = producto(11L, "Jugo", "8.00", categoria(1L));
        producto.setImagenDatos(new byte[]{1,2,3});
        producto.setImagenTipo("image/png");
        when(repository.findById(11L)).thenReturn(Optional.of(producto));
        when(repository.save(producto)).thenReturn(producto);

        servicio.eliminarImagen(11L);

        assertNull(producto.getImagenDatos());
        assertNull(producto.getImagenTipo());
        verify(repository).save(producto);
    }

    @Test void eliminaProductoExistente() {
        Producto producto = producto(12L, "Temporal", "5.00", categoria(1L));
        when(repository.findById(12L)).thenReturn(Optional.of(producto));
        servicio.eliminar(12L);
        verify(repository).delete(producto);
    }

    @Test void noEliminaProductoInexistente() {
        when(repository.findById(13L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> servicio.eliminar(13L));
        verify(repository, never()).delete(any());
    }

    private Categoria categoria(Long id) {
        Categoria categoria = new Categoria("Comidas", "Platos");
        categoria.setId(id);
        return categoria;
    }

    private Producto producto(Long id, String nombre, String precio, Categoria categoria) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setCategoria(categoria);
        producto.setDisponible(true);
        return producto;
    }
}
