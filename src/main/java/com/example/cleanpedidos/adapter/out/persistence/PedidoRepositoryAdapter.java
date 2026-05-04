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
import java.util.UUID;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository jpaRepository;

    public PedidoRepositoryAdapter(PedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Pedido pedido) {
        PedidoJpaEntity entity = toEntity(pedido);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Pedido> buscarPorId(PedidoId pedidoId) {
        return jpaRepository.findById(pedidoId.valor().toString()).map(this::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private Pedido toDomain(PedidoJpaEntity entity) {
        PedidoId pedidoId = new PedidoId(UUID.fromString(entity.getId()));
        Pedido pedido = new Pedido(pedidoId, entity.getClienteNombre());

        entity.getLineas().forEach(linea ->
                pedido.agregarLinea(
                        linea.getProductoNombre(),
                        linea.getCantidad(),
                        new Dinero(linea.getPrecioUnitario())
                )
        );

        if (EstadoPedido.CONFIRMADO.name().equals(entity.getEstado())) {
            pedido.confirmar();
        } else if (EstadoPedido.CANCELADO.name().equals(entity.getEstado())) {
            // Si necesitamos cancelar, aquí iría la lógica, pero por ahora solo soportamos BORRADOR y CONFIRMADO
            pedido.confirmar(); // Al confirmar se lleva a CONFIRMADO, luego podríamos cancelar si hubiera un método
        }

        return pedido;
    }

    private PedidoJpaEntity toEntity(Pedido pedido) {
        PedidoJpaEntity entity = new PedidoJpaEntity(
                pedido.getId().valor().toString(),
                pedido.getClienteNombre(),
                pedido.getEstado().name(),
                pedido.getLineas().stream()
                        .map(linea -> new PedidoJpaEntity.LineaPedidoEmbeddable(
                                linea.productoNombre(),
                                linea.cantidad(),
                                linea.precioUnitario().cantidad()
                        ))
                        .toList()
        );
        return entity;
    }
}