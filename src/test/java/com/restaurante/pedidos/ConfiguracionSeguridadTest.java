package com.restaurante.pedidos;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionSeguridadTest {

    @Test void codificaContrasenaYNoLaGuardaEnTextoPlano() {
        ConfiguracionSeguridad configuracion = new ConfiguracionSeguridad();
        PasswordEncoder encoder = configuracion.passwordEncoder();

        String hash = encoder.encode("ClaveSegura123!");

        assertNotEquals("ClaveSegura123!", hash);
        assertTrue(encoder.matches("ClaveSegura123!", hash));
        assertFalse(encoder.matches("otra-clave", hash));
    }

    @Test void creaUsuarioAdministradorConRolAdmin() {
        ConfiguracionSeguridad configuracion = new ConfiguracionSeguridad();
        PasswordEncoder encoder = configuracion.passwordEncoder();

        var servicio = configuracion.userDetailsService(encoder, "admin-prueba", "ClaveSegura123!");
        UserDetails usuario = servicio.loadUserByUsername("admin-prueba");

        assertEquals("admin-prueba", usuario.getUsername());
        assertTrue(usuario.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(encoder.matches("ClaveSegura123!", usuario.getPassword()));
    }
}
