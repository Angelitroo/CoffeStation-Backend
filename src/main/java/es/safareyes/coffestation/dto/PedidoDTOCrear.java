package es.safareyes.coffestation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record PedidoDTOCrear(
        @Positive(message = "El id de cupón debe ser positivo")
        Long cuponId,
        @NotEmpty(message = "El pedido debe tener al menos una línea")
        List<@Valid LineaPedidoDTOCrear> lineas
) {}
