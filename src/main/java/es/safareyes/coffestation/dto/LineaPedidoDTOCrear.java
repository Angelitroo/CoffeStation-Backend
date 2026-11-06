package es.safareyes.coffestation.dto;

public record LineaPedidoDTOCrear(
        Long idPedido,
        Long idProducto,
        Integer cantidad
) {}
