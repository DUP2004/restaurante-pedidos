package com.restaurante.pedidos.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DetallePedidoRequestValidationTest {
    private static jakarta.validation.ValidatorFactory factory;
    private static Validator validator;
    @BeforeAll static void iniciar() { factory = Validation.buildDefaultValidatorFactory(); validator = factory.getValidator(); }
    @AfterAll static void cerrar() { factory.close(); }

    @Test void aceptaCantidadPositivaYProducto() {
        DetallePedidoRequest d = new DetallePedidoRequest(); d.setProductoId(1L); d.setCantidad(2);
        assertTrue(validator.validate(d).isEmpty());
    }
    @Test void rechazaCantidadCero() {
        DetallePedidoRequest d = new DetallePedidoRequest(); d.setProductoId(1L); d.setCantidad(0);
        assertTrue(validator.validate(d).stream().anyMatch(v -> v.getPropertyPath().toString().equals("cantidad")));
    }
    @Test void rechazaCantidadNegativa() {
        DetallePedidoRequest d = new DetallePedidoRequest(); d.setProductoId(1L); d.setCantidad(-4);
        assertTrue(validator.validate(d).stream().anyMatch(v -> v.getPropertyPath().toString().equals("cantidad")));
    }
    @Test void rechazaProductoNulo() {
        DetallePedidoRequest d = new DetallePedidoRequest(); d.setCantidad(1);
        assertTrue(validator.validate(d).stream().anyMatch(v -> v.getPropertyPath().toString().equals("productoId")));
    }
}
