package com.example.cleanpedidos.domain.valueobject;

import java.util.Objects;

public record PedidoId(Long value) {

    public PedidoId {
        Objects.requireNonNull(value, "El id del pedido es obligatorio");
        if (value <= 0) {
            throw new IllegalArgumentException("El id del pedido debe ser mayor que cero");
        }
    }

    public static PedidoId of(Long value) {
        return new PedidoId(value);
    }
}