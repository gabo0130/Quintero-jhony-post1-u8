package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.dto.LineaPedidoDto;
import com.example.cleanpedidos.usecase.dto.PedidoResponse;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        PedidoId pedidoId = crearPedidoService.ejecutar(
                "Ana García",
                List.of(new LineaPedidoDto("Laptop", 1, new BigDecimal("1500.00")))
        );

        assertTrue(pedidoId.valor() instanceof UUID);

        Optional<PedidoResponse> consultado = consultarPedidoService.buscarPorId(pedidoId);

        assertTrue(consultado.isPresent());
        assertEquals("Ana García", consultado.get().clienteNombre());
        assertEquals("CONFIRMADO", consultado.get().estado());
        assertEquals(new BigDecimal("1500.00"), consultado.get().total());
    }

    private static final class InMemoryPedidoRepository implements PedidoRepositoryPort {

        private final Map<UUID, Pedido> data = new HashMap<>();

        @Override
        public void guardar(Pedido pedido) {
            data.put(pedido.getId().valor(), pedido);
        }

        @Override
        public Optional<Pedido> buscarPorId(PedidoId pedidoId) {
            return Optional.ofNullable(data.get(pedidoId.valor()));
        }

        @Override
        public List<Pedido> buscarTodos() {
            return new ArrayList<>(data.values());
        }
    }
}