package es.safareyes.coffestation.dto;

import es.safareyes.coffestation.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private LocalDateTime fecha;
    private Integer numeroTurno;
    private Estado estado;
    private Long cuponId;
}
