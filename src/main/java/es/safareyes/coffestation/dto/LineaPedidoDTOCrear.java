package es.safareyes.coffestation.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LineaPedidoDTOCrear(
        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El id de producto debe ser positivo")
        Long idProducto,

        // RN-04: cada línea entre 1 y 20 unidades
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima es 1")
        @Max(value = 20, message = "La cantidad máxima es 20")
        Integer cantidad,

        @NotNull(message = "El precio sin IVA es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        @Digits(integer = 6, fraction = 2)
        BigDecimal precioSinIva,

        @NotNull(message = "El IVA es obligatorio")
        @PositiveOrZero(message = "El IVA no puede ser negativo")
        @Digits(integer = 3, fraction = 2)
        BigDecimal iva
) {}

