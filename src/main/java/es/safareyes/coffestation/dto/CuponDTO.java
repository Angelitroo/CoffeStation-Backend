package es.safareyes.coffestation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.safareyes.coffestation.enums.Tipo;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuponDTO(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 30, message = "El código no puede superar 30 caracteres")
        String codigo,

        @NotNull(message = "El tipo es obligatorio (PORCENTAJE o IMPORTE_FIJO)")
        Tipo tipo,

        @NotNull(message = "El valor es obligatorio")
        @Positive(message = "El valor debe ser mayor que 0")
        @Digits(integer = 4, fraction = 2, message = "El valor admite máximo 4 enteros y 2 decimales")
        BigDecimal valor,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDateTime fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        @FutureOrPresent(message = "La fecha de fin no puede estar en el pasado")
        LocalDateTime fechaFin,

        @NotNull(message = "El número máximo de usos es obligatorio")
        @Positive(message = "El número máximo de usos debe ser mayor que 0")
        Integer maxUsos,

        @PositiveOrZero(message = "El importe mínimo no puede ser negativo")
        @Digits(integer = 6, fraction = 2, message = "El importe mínimo admite máximo 6 enteros y 2 decimales")
        BigDecimal importeMinimo,

        @Positive(message = "El id de cliente debe ser positivo")
        Long clienteId
)
{}
