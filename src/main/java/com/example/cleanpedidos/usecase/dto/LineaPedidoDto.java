package com.example.cleanpedidos.usecase.dto;

import java.math.BigDecimal;

public record LineaPedidoDto(String productoNombre, int cantidad, BigDecimal precioUnitario) {
}
