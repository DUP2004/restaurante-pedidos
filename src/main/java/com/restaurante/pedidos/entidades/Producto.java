package com.restaurante.pedidos.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean disponible = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Las imágenes pequeñas se almacenan en la BD para no depender del disco efímero de Render.
    @JsonIgnore
    @Column(name = "imagen_datos", columnDefinition = "MEDIUMBLOB")
    private byte[] imagenDatos;

    @JsonIgnore
    @Column(name = "imagen_tipo", length = 30)
    private String imagenTipo;

    public Producto() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
    public Categoria getCategoria() { return categoria; }
    @JsonProperty("imagenUrl")
    public String getImagenUrl() { return imagenDatos == null ? null : "/api/productos/" + id + "/imagen"; }
    @JsonIgnore
    public byte[] getImagenDatos() { return imagenDatos; }
    @JsonIgnore
    public String getImagenTipo() { return imagenTipo; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setImagenDatos(byte[] imagenDatos) { this.imagenDatos = imagenDatos; }
    public void setImagenTipo(String imagenTipo) { this.imagenTipo = imagenTipo; }
}
