package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.ProductoRankingDTO;
import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class LineaPedidoService {

    private LineaPedidoRepository lineaPedidoRepository;

    public ResumenDiaDTO getResumenDiaByFecha(LocalDate fecha) {
        return lineaPedidoRepository.getResumenDiaByFecha(fecha);
    }

    public List<ProductoRankingDTO> getProductosMasVendidos(LocalDate fechaMinima, LocalDate fechaMaxima) {
        return lineaPedidoRepository.getProductosMasVendidos(fechaMinima, fechaMaxima);
    }
}
