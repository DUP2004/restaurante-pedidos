package com.restaurante.pedidos.controladores;

import com.restaurante.pedidos.entidades.Producto;
import com.restaurante.pedidos.servicio.ProductoSer;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoCont {
    private final ProductoSer service;
    public ProductoCont(ProductoSer service) { this.service = service; }

    @GetMapping public List<Producto> listar() { return service.listar(); }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Producto guardar(@Valid @RequestBody Producto p) { return service.guardar(p); }
    @PutMapping("/{id}") public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto p) { return service.actualizar(id, p); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Producto subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) throw new IllegalArgumentException("Selecciona una imagen.");
        return service.guardarImagen(id, archivo.getBytes(), archivo.getContentType());
    }

    @DeleteMapping("/{id}/imagen")
    public Producto eliminarImagen(@PathVariable Long id) { return service.eliminarImagen(id); }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        Producto producto = service.obtener(id);
        byte[] datos = producto.getImagenDatos();
        if (datos == null || datos.length == 0) return ResponseEntity.notFound().build();
        MediaType tipo = MediaType.parseMediaType(producto.getImagenTipo());
        return ResponseEntity.ok()
                .contentType(tipo)
                .cacheControl(CacheControl.noCache())
                .header("X-Content-Type-Options", "nosniff")
                .body(datos);
    }
}
