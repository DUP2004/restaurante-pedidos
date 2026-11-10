package com.restaurante.pedidos.servicio;

import java.util.Arrays;
import java.util.Locale;

/** Valida el tipo real por firma binaria; nunca confía en el nombre del archivo. */
public final class ImagenProductoValidator {
    public static final int MAX_BYTES = 3 * 1024 * 1024;

    private ImagenProductoValidator() {}

    public static String validar(byte[] datos, String tipoDeclarado) {
        if (datos == null || datos.length == 0) {
            throw new IllegalArgumentException("Selecciona una imagen.");
        }
        if (datos.length > MAX_BYTES) {
            throw new IllegalArgumentException("La imagen supera el límite de 3 MB.");
        }
        String tipo = tipoDeclarado == null ? "" : tipoDeclarado.toLowerCase(Locale.ROOT);
        boolean jpeg = tipo.equals("image/jpeg") && datos.length >= 3
                && (datos[0] & 0xff) == 0xff && (datos[1] & 0xff) == 0xd8 && (datos[2] & 0xff) == 0xff;
        boolean png = tipo.equals("image/png") && datos.length >= 8
                && Arrays.equals(Arrays.copyOf(datos, 8), new byte[]{(byte)0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a});
        boolean webp = tipo.equals("image/webp") && datos.length >= 12
                && ascii(datos, 0, "RIFF") && ascii(datos, 8, "WEBP");
        if (jpeg) return "image/jpeg";
        if (png) return "image/png";
        if (webp) return "image/webp";
        throw new IllegalArgumentException("Formato no permitido. Usa una imagen JPEG, PNG o WebP válida.");
    }

    private static boolean ascii(byte[] datos, int inicio, String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (datos[inicio + i] != (byte) texto.charAt(i)) return false;
        }
        return true;
    }
}
