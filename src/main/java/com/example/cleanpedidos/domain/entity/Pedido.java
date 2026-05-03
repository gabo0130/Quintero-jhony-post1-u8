package com.example.cleanpedidos.domain.entity;

import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class Pedido {

    private final PedidoId id;
    private final String cliente;
    private final List<LineaPedido> lineas;
    private final EstadoPedido estado;
    private final Dinero total;

    public Pedido(PedidoId id, String cliente, List<LineaPedido> lineas, EstadoPedido estado) {
        this.id = id;
        this.cliente = validarCliente(cliente);
        this.lineas = List.copyOf(validarLineas(lineas));
        this.estado = Objects.requireNonNull(estado, "El estado del pedido es obligatorio");
        this.total = calcularTotal(this.lineas);
    }

    public static Pedido crearNuevo(String cliente, List<LineaPedido> lineas) {
        return new Pedido(null, cliente, lineas, EstadoPedido.CREADO);
    }

    public Pedido conId(PedidoId nuevoId) {
        return new Pedido(Objects.requireNonNull(nuevoId, "El id del pedido es obligatorio"), cliente, lineas, estado);
    }

    public Pedido conEstado(EstadoPedido nuevoEstado) {
        return new Pedido(id, cliente, lineas, nuevoEstado);
    }

    public PedidoId getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Dinero getTotal() {
        return total;
    }

    private static String validarCliente(String cliente) {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        return cliente;
    }

    private static List<LineaPedido> validarLineas(List<LineaPedido> lineas) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos una línea");
        }
        return lineas;
    }

    private static Dinero calcularTotal(List<LineaPedido> lineas) {
        BigDecimal total = lineas.stream()
                .map(LineaPedido::subtotal)
                .map(Dinero::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Dinero.of(total);
    }
}