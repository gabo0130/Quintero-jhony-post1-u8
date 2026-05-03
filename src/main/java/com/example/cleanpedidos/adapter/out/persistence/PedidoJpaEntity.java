package com.example.cleanpedidos.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String cliente;

    @Column(nullable = false, length = 40)
    private String estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pedido_lineas", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<LineaPedidoEmbeddable> lineas = new ArrayList<>();

    public PedidoJpaEntity() {
    }

    public PedidoJpaEntity(Long id, String cliente, String estado, BigDecimal total, List<LineaPedidoEmbeddable> lineas) {
        this.id = id;
        this.cliente = cliente;
        this.estado = estado;
        this.total = total;
        this.lineas = lineas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<LineaPedidoEmbeddable> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedidoEmbeddable> lineas) {
        this.lineas = lineas;
    }

    @Embeddable
    public static class LineaPedidoEmbeddable {

        @Column(nullable = false, length = 80)
        private String referencia;

        @Column(nullable = false, length = 255)
        private String descripcion;

        @Column(nullable = false)
        private int cantidad;

        @Column(nullable = false, precision = 12, scale = 2)
        private BigDecimal precioUnitario;

        public LineaPedidoEmbeddable() {
        }

        public LineaPedidoEmbeddable(String referencia, String descripcion, int cantidad, BigDecimal precioUnitario) {
            this.referencia = referencia;
            this.descripcion = descripcion;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }

        public String getReferencia() {
            return referencia;
        }

        public void setReferencia(String referencia) {
            this.referencia = referencia;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }
}