package es.safareyes.coffestation.dto;

import java.time.LocalDateTime;

/*
    Un cupón es válido si existe, está activo, la fecha actual está dentro de su
    vigencia,no ha alcanzado sus usos máximos, el subtotal supera su importe mínimo y,
    si es personal,el email coincide con el del cliente.
*/
public record CuponValidarDTO(
        String codigo,
        LocalDateTime fechaFin,
        Integer maxUsos
) {}
