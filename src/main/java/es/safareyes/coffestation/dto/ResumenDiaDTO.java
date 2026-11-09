package es.safareyes.coffestation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumenDiaDTO(
        LocalDate fecha,
        Long numeroPedidos,
        BigDecimal base,        // Precio con IVA
        BigDecimal total          // Precio sin IVA
) {}
