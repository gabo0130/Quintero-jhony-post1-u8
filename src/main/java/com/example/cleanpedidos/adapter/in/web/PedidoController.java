package com.example.cleanpedidos.adapter.in.web;

import com.example.cleanpedidos.adapter.in.web.dto.CrearPedidoRequest;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.dto.LineaPedidoDto;
import com.example.cleanpedidos.usecase.dto.PedidoResponse;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final ConsultarPedidoUseCase consultarPedidoUseCase;

    public PedidoController(CrearPedidoUseCase crearPedidoUseCase, ConsultarPedidoUseCase consultarPedidoUseCase) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.consultarPedidoUseCase = consultarPedidoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crear(@Valid @RequestBody CrearPedidoRequest request) {
        List<LineaPedidoDto> lineas = request.lineas().stream()
                .map(linea -> new LineaPedidoDto(
                        linea.productoNombre(),
                        linea.cantidad(),
                        linea.precioUnitario()
                ))
                .collect(Collectors.toList());

        PedidoId pedidoId = crearPedidoUseCase.ejecutar(request.clienteNombre(), lineas);
        return Map.of("pedidoId", pedidoId.toString());
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable String id) {
        PedidoId pedidoId = new PedidoId(UUID.fromString(id));
        return consultarPedidoUseCase.buscarPorId(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido %s no encontrado".formatted(id)));
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return consultarPedidoUseCase.listarTodos();
    }
}