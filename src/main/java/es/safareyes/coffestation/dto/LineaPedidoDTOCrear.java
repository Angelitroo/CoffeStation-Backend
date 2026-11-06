package es.safareyes.coffestation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LineaPedidoDTOCrear(
        @NotNull(message = "El pedido es obligatorio")
        @Positive(message = "El id de pedido debe ser positivo")
        Long idPedido,

        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El id de producto debe ser positivo")
        Long idProducto,

        // RN-04: cada línea entre 1 y 20 unidades
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima es 1")
        @Max(value = 20, message = "La cantidad máxima es 20")
        Integer cantidad
) {}
