package com.example.cleanpedidos.adapter.out.persistence;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository jpaRepository;

    public PedidoRepositoryAdapter(PedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoJpaEntity entity = toEntity(pedido);
        PedidoJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Pedido> buscarPorId(PedidoId pedidoId) {
        return jpaRepository.findById(pedidoId.value()).map(this::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private Pedido toDomain(PedidoJpaEntity entity) {
        List<LineaPedido> lineas = entity.getLineas().stream()
                .map(linea -> new LineaPedido(
                        linea.getReferencia(),
                        linea.getDescripcion(),
                        linea.getCantidad(),
                        Dinero.of(linea.getPrecioUnitario())
                ))
                .toList();
        return new Pedido(
                entity.getId() == null ? null : PedidoId.of(entity.getId()),
                entity.getCliente(),
                lineas,
                EstadoPedido.valueOf(entity.getEstado())
        );
    }

    private PedidoJpaEntity toEntity(Pedido pedido) {
        PedidoJpaEntity entity = new PedidoJpaEntity();
        if (pedido.getId() != null) {
            entity.setId(pedido.getId().value());
        }
        entity.setCliente(pedido.getCliente());
        entity.setEstado(pedido.getEstado().name());
        entity.setTotal(pedido.getTotal().valor());
        entity.setLineas(pedido.getLineas().stream()
                .map(linea -> new PedidoJpaEntity.LineaPedidoEmbeddable(
                        linea.referencia(),
                        linea.descripcion(),
                        linea.cantidad(),
                        linea.precioUnitario().valor()
                ))
                .toList());
        return entity;
    }
}