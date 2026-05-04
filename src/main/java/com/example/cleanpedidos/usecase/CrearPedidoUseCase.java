package com.example.cleanpedidos.usecase;

import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.dto.LineaPedidoDto;

import java.util.List;

public interface CrearPedidoUseCase {

    PedidoId ejecutar(String clienteNombre, List<LineaPedidoDto> lineas);
}