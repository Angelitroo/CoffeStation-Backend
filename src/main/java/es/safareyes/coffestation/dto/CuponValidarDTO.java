package es.safareyes.coffestation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/*
    Un cupón es válido si existe, está activo, la fecha actual está dentro de su
    vigencia,no ha alcanzado sus usos máximos, el subtotal supera su importe mínimo y,
    si es personal,el email coincide con el del cliente.
*/
public record CuponValidarDTO(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 30, message = "El código no puede superar 30 caracteres")
        String codigo,

        LocalDateTime fechaFin,

        @Positive(message = "El número máximo de usos debe ser mayor que 0")
        Integer maxUsos
) {}
