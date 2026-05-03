package com.example.cleanpedidos.config;

import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.impl.CrearPedidoService;
import com.example.cleanpedidos.usecase.impl.ConsultarPedidoService;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoConfiguration {

    @Bean
    public CrearPedidoUseCase crearPedidoUseCase(PedidoRepositoryPort repositoryPort) {
        return new CrearPedidoService(repositoryPort);
    }

    @Bean
    public ConsultarPedidoUseCase consultarPedidoUseCase(PedidoRepositoryPort repositoryPort) {
        return new ConsultarPedidoService(repositoryPort);
    }
}