package es.safareyes.coffestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenDiaDTO {
    private LocalDate fecha;
    private Long numeroPedidos;
    private BigDecimal total;        // Precio con IVA
    private BigDecimal base;         // Precio sin IVA
}
