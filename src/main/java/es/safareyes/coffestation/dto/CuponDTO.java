package es.safareyes.coffestation.dto;

import es.safareyes.coffestation.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuponDTO {
    private String codigo;
    private Tipo tipo;
    private BigDecimal valor;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer maxUsos;
    private BigDecimal importeMinimo;
    private Long clienteId;
}
