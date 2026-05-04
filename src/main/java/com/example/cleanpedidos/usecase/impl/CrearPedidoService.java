package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.dto.LineaPedidoDto;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;

import java.util.List;
import java.util.Objects;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepositoryPort repositoryPort;

    public CrearPedidoService(PedidoRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "El repositorio de pedidos es obligatorio");
    }

    @Override
    public PedidoId ejecutar(String clienteNombre, List<LineaPedidoDto> lineas) {
        PedidoId pedidoId = PedidoId.nuevo();
        Pedido pedido = new Pedido(pedidoId, clienteNombre);

        lineas.forEach(linea -> pedido.agregarLinea(
                linea.productoNombre(),
                linea.cantidad(),
                new Dinero(linea.precioUnitario())
        ));

        pedido.confirmar();
        repositoryPort.guardar(pedido);

        return pedidoId;
    }
}