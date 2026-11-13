package com.restaurante.pedidos.controladores;

import com.restaurante.pedidos.entidades.Producto;
import com.restaurante.pedidos.servicio.ProductoSer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoContTest {
    @Mock ProductoSer service;
    @InjectMocks ProductoCont controller;

    @Test void listaProductos() {
        when(service.listar()).thenReturn(List.of(new Producto(), new Producto()));
        assertEquals(2, controller.listar().size());
    }

    @Test void obtieneProductoPorId() {
        Producto producto = new Producto();
        when(service.obtener(10L)).thenReturn(producto);
        assertSame(producto, controller.obtener(10L));
    }

    @Test void creaProducto() {
        Producto producto = new Producto();
        when(service.guardar(producto)).thenReturn(producto);
        assertSame(producto, controller.guardar(producto));
        verify(service).guardar(producto);
    }

    @Test void actualizaProducto() {
        Producto producto = new Producto();
        when(service.actualizar(2L, producto)).thenReturn(producto);
        assertSame(producto, controller.actualizar(2L, producto));
    }

    @Test void eliminaProducto() {
        assertDoesNotThrow(() -> controller.eliminar(3L));
        verify(service).eliminar(3L);
    }

    @Test void subeArchivoDeImagenYEntregaBytesAlServicio() throws Exception {
        byte[] png = {(byte)0x89,0x50,0x4e,0x47,0x0d,0x0a,0x1a,0x0a};
        MockMultipartFile archivo = new MockMultipartFile("archivo", "foto.png", "image/png", png);
        Producto producto = new Producto();
        when(service.guardarImagen(4L, png, "image/png")).thenReturn(producto);

        assertSame(producto, controller.subirImagen(4L, archivo));
        verify(service).guardarImagen(4L, png, "image/png");
    }

    @Test void rechazaArchivoVacioAntesDeLlamarAlServicio() {
        MockMultipartFile archivo = new MockMultipartFile("archivo", "vacio.png", "image/png", new byte[0]);
        assertEquals("Selecciona una imagen.",
                assertThrows(IllegalArgumentException.class, () -> controller.subirImagen(4L, archivo)).getMessage());
        verifyNoInteractions(service);
    }

    @Test void devuelve404CuandoProductoNoTieneImagen() {
        Producto producto = new Producto();
        when(service.obtener(5L)).thenReturn(producto);
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerImagen(5L).getStatusCode());
    }

    @Test void devuelveImagenConCabecerasDeSeguridad() {
        Producto producto = new Producto();
        producto.setImagenDatos(new byte[]{1,2,3});
        producto.setImagenTipo("image/png");
        when(service.obtener(6L)).thenReturn(producto);

        var respuesta = controller.obtenerImagen(6L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("image/png", respuesta.getHeaders().getContentType().toString());
        assertEquals("nosniff", respuesta.getHeaders().getFirst("X-Content-Type-Options"));
        assertArrayEquals(new byte[]{1,2,3}, respuesta.getBody());
    }

    @Test void eliminaImagen() {
        Producto producto = new Producto();
        when(service.eliminarImagen(7L)).thenReturn(producto);
        assertSame(producto, controller.eliminarImagen(7L));
    }
}
