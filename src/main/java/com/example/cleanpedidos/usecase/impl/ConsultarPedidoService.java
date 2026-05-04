package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.dto.PedidoResponse;
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
    public Optional<PedidoResponse> buscarPorId(PedidoId pedidoId) {
        return repositoryPort.buscarPorId(Objects.requireNonNull(pedidoId, "El id del pedido es obligatorio"))
                .map(this::toPedidoResponse);
    }

    @Override
    public List<PedidoResponse> listarTodos() {
        return repositoryPort.buscarTodos().stream()
                .map(this::toPedidoResponse)
                .toList();
    }

    private PedidoResponse toPedidoResponse(Pedido pedido) {
        List<PedidoResponse.LineaPedidoResponse> lineas = pedido.getLineas().stream()
                .map(linea -> new PedidoResponse.LineaPedidoResponse(
                        linea.productoNombre(),
                        linea.cantidad(),
                        linea.precioUnitario().cantidad(),
                        linea.subtotal().cantidad()
                ))
                .toList();

        return new PedidoResponse(
                pedido.getId().toString(),
                pedido.getClienteNombre(),
                pedido.getEstado().name(),
                pedido.calcularTotal().cantidad(),
                lineas
        );
    }
}