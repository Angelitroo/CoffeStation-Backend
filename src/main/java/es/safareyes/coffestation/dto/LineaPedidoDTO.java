package es.safareyes.coffestation.dto;

import java.math.BigDecimal;

public record LineaPedidoDTO(
        Long idPedido,
        Long idProducto,
        Integer cantidad,
        BigDecimal precioSinIva,
        BigDecimal iva,
        BigDecimal total
) {}
