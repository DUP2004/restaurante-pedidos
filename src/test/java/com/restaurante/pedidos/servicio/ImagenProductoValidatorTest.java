package com.restaurante.pedidos.servicio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ImagenProductoValidatorTest {
    @Test
    void aceptaFirmasValidasDeImagenesSoportadas() {
        byte[] png = {(byte)0x89,0x50,0x4e,0x47,0x0d,0x0a,0x1a,0x0a};
        byte[] jpeg = {(byte)0xff,(byte)0xd8,(byte)0xff,0x00};
        byte[] webp = {'R','I','F','F',0,0,0,0,'W','E','B','P'};
        assertEquals("image/png", ImagenProductoValidator.validar(png, "image/png"));
        assertEquals("image/jpeg", ImagenProductoValidator.validar(jpeg, "image/jpeg"));
        assertEquals("image/webp", ImagenProductoValidator.validar(webp, "image/webp"));
    }

    @Test
    void rechazaTipoQueNoCoincideConLaFirma() {
        byte[] png = {(byte)0x89,0x50,0x4e,0x47,0x0d,0x0a,0x1a,0x0a};
        assertThrows(IllegalArgumentException.class, () -> ImagenProductoValidator.validar(png, "image/jpeg"));
    }

    @Test
    void rechazaContenidoVacioOTipoNoPermitido() {
        assertThrows(IllegalArgumentException.class, () -> ImagenProductoValidator.validar(new byte[0], "image/png"));
        assertThrows(IllegalArgumentException.class, () -> ImagenProductoValidator.validar(new byte[]{1,2,3}, "application/pdf"));
    }

    @Test
    void rechazaImagenMayorA3Mb() {
        byte[] grande = new byte[ImagenProductoValidator.MAX_BYTES + 1];
        assertThrows(IllegalArgumentException.class, () -> ImagenProductoValidator.validar(grande, "image/png"));
    }
}
