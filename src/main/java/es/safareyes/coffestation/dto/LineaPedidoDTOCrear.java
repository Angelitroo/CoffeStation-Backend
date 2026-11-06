package es.safareyes.coffestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedidoDTOCrear {
    private Long idPedido;
    private Long idProducto;
    private Integer cantidad;
}
