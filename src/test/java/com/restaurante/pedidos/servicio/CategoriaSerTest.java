package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.entidades.Categoria;
import com.restaurante.pedidos.repositorio.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaSerTest {
    @Mock CategoriaRepository repository;
    @InjectMocks CategoriaSer servicio;

    @Test void listaTodasLasCategorias() {
        when(repository.findAll()).thenReturn(List.of(new Categoria("Bebidas", "Frías")));
        assertEquals(1, servicio.listar().size());
        verify(repository).findAll();
    }

    @Test void obtieneCategoriaExistente() {
        Categoria categoria = new Categoria("Postres", "Dulces");
        when(repository.findById(2L)).thenReturn(Optional.of(categoria));
        assertSame(categoria, servicio.obtener(2L));
    }

    @Test void lanzaErrorCuandoCategoriaNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException error = assertThrows(RuntimeException.class, () -> servicio.obtener(99L));
        assertEquals("Categoría no encontrada", error.getMessage());
    }

    @Test void guardaCategoriaNueva() {
        Categoria categoria = new Categoria("Entradas", "Para compartir");
        when(repository.save(categoria)).thenReturn(categoria);
        assertSame(categoria, servicio.guardar(categoria));
        verify(repository).save(categoria);
    }

    @Test void actualizaNombreYDescripcion() {
        Categoria existente = new Categoria("Antiguo", "Anterior");
        Categoria cambios = new Categoria("Nuevo", "Actualizado");
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Categoria resultado = servicio.actualizar(3L, cambios);

        assertSame(existente, resultado);
        assertEquals("Nuevo", resultado.getNombre());
        assertEquals("Actualizado", resultado.getDescripcion());
        verify(repository).save(existente);
    }

    @Test void noActualizaSiCategoriaNoExiste() {
        when(repository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> servicio.actualizar(4L, new Categoria("Nuevo", "Descripción")));
        verify(repository, never()).save(any());
    }

    @Test void eliminaCategoriaExistente() {
        Categoria categoria = new Categoria("Temporal", "Eliminar");
        when(repository.findById(5L)).thenReturn(Optional.of(categoria));
        servicio.eliminar(5L);
        verify(repository).delete(categoria);
    }

    @Test void noEliminaSiCategoriaNoExiste() {
        when(repository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> servicio.eliminar(6L));
        verify(repository, never()).delete(any());
    }
}
