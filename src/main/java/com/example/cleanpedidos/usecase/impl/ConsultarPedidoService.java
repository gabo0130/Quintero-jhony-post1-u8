package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConsultarPedidoService implements ConsultarPedidoUseCase {

    private final PedidoRepositoryPort repositoryPort;

    public ConsultarPedidoService(PedidoRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "El repositorio de pedidos es obligatorio");
    }

    @Override
    public Optional<Pedido> consultarPedido(PedidoId pedidoId) {
        return repositoryPort.buscarPorId(Objects.requireNonNull(pedidoId, "El id del pedido es obligatorio"));
    }

    @Override
    public List<Pedido> listarPedidos() {
        return repositoryPort.buscarTodos();
    }
}