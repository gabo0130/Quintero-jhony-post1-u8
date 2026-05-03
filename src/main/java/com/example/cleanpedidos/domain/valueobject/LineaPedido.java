package com.example.cleanpedidos.domain.valueobject;

import java.util.Objects;

public record LineaPedido(String referencia, String descripcion, int cantidad, Dinero precioUnitario) {

    public LineaPedido {
        if (referencia == null || referencia.isBlank()) {
            throw new IllegalArgumentException("La referencia del producto es obligatoria");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del producto es obligatoria");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        precioUnitario = Objects.requireNonNull(precioUnitario, "El precio unitario es obligatorio");
    }

    public Dinero subtotal() {
        return precioUnitario.multiplicar(cantidad);
    }
}