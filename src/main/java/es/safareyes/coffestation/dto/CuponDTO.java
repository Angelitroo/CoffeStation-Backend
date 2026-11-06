package es.safareyes.coffestation.dto;

import es.safareyes.coffestation.enums.Tipo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuponDTO(
        String codigo,
        Tipo tipo,
        BigDecimal valor,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Integer maxUsos,
        BigDecimal importeMinimo,
        Long clienteId
) {}
