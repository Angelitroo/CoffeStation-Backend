package es.safareyes.coffestation.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductoDTOcrear(
        String nombre,
        String descripcion,
        BigDecimal precio,
        BigDecimal iva,
        Boolean disponible,
        Boolean activo,
        Long categoriaId,
        List<Long> alergenosIds
) {}
