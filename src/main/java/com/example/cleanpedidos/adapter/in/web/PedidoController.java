package com.example.cleanpedidos.adapter.in.web;

import com.example.cleanpedidos.adapter.in.web.dto.CrearPedidoRequest;
import com.example.cleanpedidos.adapter.in.web.dto.PedidoResponse;
import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final ConsultarPedidoUseCase consultarPedidoUseCase;

    public PedidoController(CrearPedidoUseCase crearPedidoUseCase, ConsultarPedidoUseCase consultarPedidoUseCase) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.consultarPedidoUseCase = consultarPedidoUseCase;
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return consultarPedidoUseCase.listarPedidos().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        Pedido pedido = consultarPedidoUseCase.consultarPedido(PedidoId.of(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido %s no encontrado".formatted(id)));
        return toResponse(pedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(@Valid @RequestBody CrearPedidoRequest request) {
        Pedido pedido = toDomain(request);
        Pedido guardado = crearPedidoUseCase.crearPedido(pedido);
        return toResponse(guardado);
    }

    private Pedido toDomain(CrearPedidoRequest request) {
        List<LineaPedido> lineas = request.lineas().stream()
                .map(linea -> new LineaPedido(
                        linea.referencia(),
                        linea.descripcion(),
                        linea.cantidad(),
                        Dinero.of(linea.precioUnitario())
                ))
                .toList();
        return Pedido.crearNuevo(request.cliente(), lineas);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId() == null ? null : pedido.getId().value(),
                pedido.getCliente(),
                pedido.getEstado().name(),
                pedido.getTotal().valor(),
                pedido.getLineas().stream().map(linea -> new PedidoResponse.LineaPedidoResponse(
                        linea.referencia(),
                        linea.descripcion(),
                        linea.cantidad(),
                        linea.precioUnitario().valor(),
                        linea.subtotal().valor()
                )).toList()
        );
    }
}