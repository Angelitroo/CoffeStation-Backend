package es.safareyes.coffestation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumenDiaDTO(
        LocalDate fecha,
        Long numeroPedidos,
        BigDecimal total,        // Precio con IVA
        BigDecimal base          // Precio sin IVA
) {}
