package es.safareyes.coffestation.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Size(max = 150, message = "La descripción no puede superar 150 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor que 0")
        @Digits(integer = 6, fraction = 2, message = "El precio admite máximo 6 enteros y 2 decimales")
        BigDecimal precioSinIva,

        @NotNull(message = "El IVA es obligatorio")
        @PositiveOrZero(message = "El IVA no puede ser negativo")
        @Digits(integer = 3, fraction = 2, message = "El IVA admite máximo 3 enteros y 2 decimales")
        BigDecimal iva,

        Boolean disponible,
        Boolean activo
) {}
