package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;

import java.util.Objects;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepositoryPort repositoryPort;

    public CrearPedidoService(PedidoRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "El repositorio de pedidos es obligatorio");
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        return repositoryPort.guardar(Objects.requireNonNull(pedido, "El pedido es obligatorio"));
    }
}