package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.ProductoRankingDTO;
import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.service.LineaPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/informe")
@AllArgsConstructor
public class LineaPedidoController {

    private LineaPedidoService lineaPedidoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'BARISTA')")
    @GetMapping("/resumen/{fecha}")
    public ResponseEntity<ResumenDiaDTO> getResumenDiaByFecha(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(lineaPedidoService.getResumenDiaByFecha(fecha));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ranking")
    public List<ProductoRankingDTO> getProductosMasVendidos(LocalDate fechaMinima, LocalDate fechaMaxima) {
        return lineaPedidoService.getProductosMasVendidos(fechaMinima,fechaMaxima);
    }
}
