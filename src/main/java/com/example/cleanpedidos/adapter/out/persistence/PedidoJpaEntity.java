package com.example.cleanpedidos.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
public class PedidoJpaEntity {

    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false, length = 120)
    private String clienteNombre;

    @Column(nullable = false, length = 40)
    private String estado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pedido_lineas", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<LineaPedidoEmbeddable> lineas = new ArrayList<>();

    public PedidoJpaEntity() {
    }

    public PedidoJpaEntity(String id, String clienteNombre, String estado, List<LineaPedidoEmbeddable> lineas) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.estado = estado;
        this.lineas = lineas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<LineaPedidoEmbeddable> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedidoEmbeddable> lineas) {
        this.lineas = lineas;
    }

    @Embeddable
    public static class LineaPedidoEmbeddable {

        @Column(nullable = false, length = 120)
        private String productoNombre;

        @Column(nullable = false)
        private int cantidad;

        @Column(nullable = false, precision = 12, scale = 2)
        private BigDecimal precioUnitario;

        public LineaPedidoEmbeddable() {
        }

        public LineaPedidoEmbeddable(String productoNombre, int cantidad, BigDecimal precioUnitario) {
            this.productoNombre = productoNombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }

        public String getProductoNombre() {
            return productoNombre;
        }

        public void setProductoNombre(String productoNombre) {
            this.productoNombre = productoNombre;
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