package com.example.cleanpedidos.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CrearPedidoRequest(
        @NotBlank String clienteNombre,
        @NotEmpty @Valid List<LineaPedidoRequest> lineas
) {

    public record LineaPedidoRequest(
            @NotBlank String productoNombre,
            @Positive int cantidad,
            @Positive BigDecimal precioUnitario
    ) {
    }
}