package es.safareyes.coffestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedidoDTO {
    private Long idPedido;
    private Long idProducto;
    private Integer cantidad;
    private BigDecimal precioSinIva;
    private BigDecimal iva;
    private BigDecimal total;
}
