package com.example.cleanpedidos.usecase;

import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.dto.PedidoResponse;

import java.util.List;
import java.util.Optional;

public interface ConsultarPedidoUseCase {

    Optional<PedidoResponse> buscarPorId(PedidoId pedidoId);

    List<PedidoResponse> listarTodos();
}