package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoServiceTest {

    private CrearPedidoService crearPedidoService;
    private ConsultarPedidoService consultarPedidoService;

    @BeforeEach
    void setUp() {
        InMemoryPedidoRepository repository = new InMemoryPedidoRepository();
        crearPedidoService = new CrearPedidoService(repository);
        consultarPedidoService = new ConsultarPedidoService(repository);
    }

    @Test
    void debeCrearYConsultarPedido() {
        Pedido creado = crearPedidoService.crearPedido(Pedido.crearNuevo(
                "Cliente Uno",
                List.of(new LineaPedido("SKU-1", "Mouse", 2, Dinero.of(new BigDecimal("50.00"))))
        ));

        assertEquals(1L, creado.getId().value());
        assertEquals(new BigDecimal("100.00"), creado.getTotal().valor());

        Optional<Pedido> consultado = consultarPedidoService.consultarPedido(PedidoId.of(1L));

        assertTrue(consultado.isPresent());
        assertEquals("Cliente Uno", consultado.get().getCliente());
    }

    private static final class InMemoryPedidoRepository implements PedidoRepositoryPort {

        private final Map<Long, Pedido> data = new HashMap<>();
        private long sequence = 0L;

        @Override
        public Pedido guardar(Pedido pedido) {
            Pedido toStore = pedido.getId() == null ? pedido.conId(new PedidoId(++sequence)) : pedido;
            data.put(toStore.getId().value(), toStore);
            return toStore;
        }

        @Override
        public Optional<Pedido> buscarPorId(PedidoId pedidoId) {
            return Optional.ofNullable(data.get(pedidoId.value()));
        }

        @Override
        public List<Pedido> buscarTodos() {
            return new ArrayList<>(data.values());
        }
    }
}