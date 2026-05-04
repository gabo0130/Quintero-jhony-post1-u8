package com.example.cleanpedidos.domain.valueobject;

import java.math.BigDecimal;

public record LineaPedido(String productoNombre, int cantidad, Dinero precioUnitario) {

    public LineaPedido {
        if (productoNombre == null || productoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (precioUnitario == null) {
            throw new IllegalArgumentException("El precio unitario es obligatorio");
        }
    }

    public Dinero subtotal() {
        return new Dinero(precioUnitario.cantidad().multiply(BigDecimal.valueOf(cantidad)));
    }
}