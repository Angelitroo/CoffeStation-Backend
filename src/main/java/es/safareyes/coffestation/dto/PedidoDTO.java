package es.safareyes.coffestation.dto;

import es.safareyes.coffestation.enums.Estado;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Los importes y el turno los calcula el servidor (RN-05, RN-09), por eso no son obligatorios
public record PedidoDTO(
        LocalDateTime fecha,

        @Positive(message = "El número de turno debe ser mayor que 0")
        Integer numeroTurno,

        @PositiveOrZero(message = "La base imponible no puede ser negativa")
        @Digits(integer = 6, fraction = 2)
        BigDecimal baseImponible,

        @PositiveOrZero(message = "El subtotal no puede ser negativo")
        @Digits(integer = 6, fraction = 2)
        BigDecimal subtotal,

        // RN-07: el total nunca puede ser negativo
        @PositiveOrZero(message = "El total no puede ser negativo")
        @Digits(integer = 6, fraction = 2)
        BigDecimal total,

        Estado estado,

        @Positive(message = "El id de cupón debe ser positivo")
        Long cuponId
) {}
