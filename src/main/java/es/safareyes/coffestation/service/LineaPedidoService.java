package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.LineaPedidoDTO;
import es.safareyes.coffestation.dto.ProductoRankingDTO;
import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import es.safareyes.coffestation.repository.PedidoRepository;
import es.safareyes.coffestation.repository.ProductoRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

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
