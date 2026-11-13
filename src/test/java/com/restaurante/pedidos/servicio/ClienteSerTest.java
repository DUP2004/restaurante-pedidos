package com.restaurante.pedidos.servicio;

import com.restaurante.pedidos.entidades.Cliente;
import com.restaurante.pedidos.repositorio.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteSerTest {
    @Mock ClienteRepository repository;
    @InjectMocks ClienteSer servicio;

    @Test void listaClientes() {
        when(repository.findAll()).thenReturn(List.of(new Cliente(), new Cliente()));
        assertEquals(2, servicio.listar().size());
    }

    @Test void obtieneClienteExistente() {
        Cliente cliente = cliente("Ana", "Pérez");
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        assertSame(cliente, servicio.obtener(1L));
    }

    @Test void informaCuandoClienteNoExiste() {
        when(repository.findById(404L)).thenReturn(Optional.empty());
        RuntimeException error = assertThrows(RuntimeException.class, () -> servicio.obtener(404L));
        assertEquals("Cliente no encontrado", error.getMessage());
    }

    @Test void guardaCliente() {
        Cliente cliente = cliente("Luis", "Díaz");
        when(repository.save(cliente)).thenReturn(cliente);
        assertSame(cliente, servicio.guardar(cliente));
        verify(repository).save(cliente);
    }

    @Test void actualizaTodosLosDatosDelCliente() {
        Cliente existente = cliente("Ana", "Pérez");
        Cliente cambios = cliente("María", "Gómez");
        cambios.setCorreo("maria@example.com");
        cambios.setTelefono("987654321");
        when(repository.findById(2L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Cliente resultado = servicio.actualizar(2L, cambios);

        assertEquals("María", resultado.getNombre());
        assertEquals("Gómez", resultado.getApellido());
        assertEquals("maria@example.com", resultado.getCorreo());
        assertEquals("987654321", resultado.getTelefono());
        verify(repository).save(existente);
    }

    @Test void noActualizaClienteInexistente() {
        when(repository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> servicio.actualizar(3L, cliente("X", "Y")));
        verify(repository, never()).save(any());
    }

    @Test void eliminaClienteExistente() {
        Cliente cliente = cliente("Pedro", "López");
        when(repository.findById(4L)).thenReturn(Optional.of(cliente));
        servicio.eliminar(4L);
        verify(repository).delete(cliente);
    }

    @Test void noEliminaClienteInexistente() {
        when(repository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> servicio.eliminar(5L));
        verify(repository, never()).delete(any());
    }

    private Cliente cliente(String nombre, String apellido) {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        return cliente;
    }
}
