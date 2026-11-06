package es.safareyes.coffestation.dto;

import es.safareyes.coffestation.enums.Estado;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoDTO(
        LocalDateTime fecha,
        Integer numeroTurno,
        BigDecimal baseImponible,
        BigDecimal subtotal,
        BigDecimal total,
        Estado estado,
        Long cuponId
) {}
