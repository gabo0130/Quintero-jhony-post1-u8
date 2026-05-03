package com.example.cleanpedidos.usecase;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;

import java.util.List;
import java.util.Optional;

public interface ConsultarPedidoUseCase {

    Optional<Pedido> consultarPedido(PedidoId pedidoId);

    List<Pedido> listarPedidos();
}