package com.example.cleanpedidos.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Dinero(BigDecimal valor) {

    public Dinero {
        Objects.requireNonNull(valor, "El valor monetario es obligatorio");
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor monetario no puede ser negativo");
        }
        valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    public static Dinero of(BigDecimal valor) {
        return new Dinero(valor);
    }

    public Dinero sumar(Dinero otro) {
        return new Dinero(valor.add(Objects.requireNonNull(otro, "El dinero a sumar es obligatorio").valor()));
    }

    public Dinero multiplicar(int factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("El factor no puede ser negativo");
        }
        return new Dinero(valor.multiply(BigDecimal.valueOf(factor)));
    }
}