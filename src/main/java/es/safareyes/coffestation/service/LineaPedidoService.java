package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LineaPedidoService {
    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;

    public ResumenDiaDTO getResumenDiaByFecha(LocalDate fecha) {
        return lineaPedidoRepository.getResumenDiaByFecha(fecha);
    }
}
