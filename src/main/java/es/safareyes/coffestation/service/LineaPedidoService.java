package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.LineaPedidoDTO;
import es.safareyes.coffestation.dto.LineaPedidoDTOCrear;
import es.safareyes.coffestation.dto.ProductoRankingDTO;
import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.model.Producto;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import es.safareyes.coffestation.repository.PedidoRepository;
import es.safareyes.coffestation.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class LineaPedidoService {

    private LineaPedidoRepository lineaPedidoRepository;

    private PedidoRepository pedidoRepository;

    private ProductoRepository productoRepository;


    public ResumenDiaDTO getResumenDiaByFecha(LocalDate fecha) {
        return lineaPedidoRepository.getResumenDiaByFecha(fecha);
    }

    public List<ProductoRankingDTO> getProductosMasVendidos(LocalDate fechaMinima, LocalDate fechaMaxima) {
        return lineaPedidoRepository.getProductosMasVendidos(fechaMinima, fechaMaxima);
    }

    public LineaPedidoDTO getLineaPedidoById(Long id) {
        LineaPedido lp = lineaPedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Línea de pedido " + id + " no existe"));
        return convertToDTO(lp);
    }

    public LineaPedidoDTO createLineaPedido(LineaPedidoDTOCrear dto) {
        Pedido pedido = pedidoRepository.findById(dto.idPedido())
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido " + dto.idPedido() + " no existe"));
        Producto producto = productoRepository.findById(dto.idProducto())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto " + dto.idProducto() + " no existe"));

        BigDecimal precioSinIva = producto.getPrecioSinIva();
        BigDecimal iva = producto.getIva();
        BigDecimal cantidad = BigDecimal.valueOf(dto.cantidad());
        BigDecimal importeIva = precioSinIva.multiply(iva).movePointLeft(2);

        LineaPedido lp = new LineaPedido();
        lp.setPedido(pedido);
        lp.setProducto(producto);

        lp.setCantidad(dto.cantidad());
        lp.setPrecioSinIva(producto.getPrecioSinIva());
        lp.setIva(producto.getIva());
        BigDecimal total = precioSinIva.add(importeIva)
                .multiply(cantidad)
                .setScale(2, RoundingMode.HALF_UP);
        lp.setTotal(total);
        return convertToDTO(lineaPedidoRepository.save(lp));
    }

    public LineaPedidoDTO convertToDTO(LineaPedido lp) {
        return new LineaPedidoDTO(
                lp.getPedido().getId(),
                lp.getProducto().getId(),
                lp.getCantidad(),
                lp.getPrecioSinIva(),
                lp.getIva(),
                lp.getTotal()
        );
    }
}
