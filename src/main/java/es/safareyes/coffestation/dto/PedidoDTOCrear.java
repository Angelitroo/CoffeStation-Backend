package es.safareyes.coffestation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PedidoDTOCrear(
        @Positive(message = "El id de cupón debe ser positivo")
        Long cuponId,
        @NotEmpty(message = "El pedido debe tener al menos una línea")
        @Size(min = 1, max = 15, message = "Un pedido tiene entre 1 y 15 líneas")
        List<@Valid LineaPedidoDTOCrear> lineas
) {}
