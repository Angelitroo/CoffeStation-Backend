package es.safareyes.coffestation.dto;

import es.safareyes.coffestation.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private LocalDateTime fecha;
    private Integer numeroTurno;
    private BigDecimal baseImponible;
    private BigDecimal subtotal;
    private BigDecimal total;
    private Estado estado;
    private Long cuponId;
}
