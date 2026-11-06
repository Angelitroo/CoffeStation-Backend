package es.safareyes.coffestation.dto;

import java.math.BigDecimal;

public record ProductoDTO(
        String nombre,
        String descripcion,
        BigDecimal precioSinIva,
        BigDecimal iva,
        Boolean disponible,
        Boolean activo
) {}
