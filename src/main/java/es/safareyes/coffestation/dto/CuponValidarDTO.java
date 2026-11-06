package es.safareyes.coffestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
    Un cupón es válido si existe, está activo, la fecha actual está dentro de su
    vigencia,no ha alcanzado sus usos máximos, el subtotal supera su importe mínimo y,
    si es personal,el email coincide con el del cliente.
*/
public class CuponValidarDTO {
    private String codigo;
    private LocalDateTime fechaFin;
    private Integer maxUsos;
}
