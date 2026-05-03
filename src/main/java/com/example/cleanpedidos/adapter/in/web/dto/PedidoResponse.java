package com.example.cleanpedidos.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponse(
        Long id,
        String cliente,
        String estado,
        BigDecimal total,
        List<LineaPedidoResponse> lineas
) {

    public record LineaPedidoResponse(
            String referencia,
            String descripcion,
            int cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {
    }
}