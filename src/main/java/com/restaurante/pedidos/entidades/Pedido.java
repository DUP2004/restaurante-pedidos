package com.restaurante.pedidos.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(
    mappedBy = "pedido",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.EAGER
    )
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {}

    public Long getId() { return id; }
    public LocalDateTime getFecha() { return fecha; }
    public EstadoPedido getEstado() { return estado; }
    public BigDecimal getTotal() { return total; }
    public Cliente getCliente() { return cliente; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setId(Long id) { this.id = id; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
