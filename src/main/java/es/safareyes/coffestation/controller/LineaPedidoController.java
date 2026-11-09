package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.LineaPedidoDTO;
import es.safareyes.coffestation.dto.LineaPedidoDTOCrear;
import es.safareyes.coffestation.dto.ProductoRankingDTO;
import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.service.LineaPedidoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/informe")
@AllArgsConstructor
public class LineaPedidoController {

    private LineaPedidoService lineaPedidoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/resumen/{fecha}")
    public ResponseEntity<ResumenDiaDTO> getResumenDiaByFecha(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(lineaPedidoService.getResumenDiaByFecha(fecha));
    }

    @GetMapping("/ranking")
    public List<ProductoRankingDTO> getProductosMasVendidos(
            @RequestParam LocalDate fechaMinima,
            @RequestParam LocalDate fechaMaxima) {
        return lineaPedidoService.getProductosMasVendidos(fechaMinima, fechaMaxima);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LineaPedidoDTO> getLineaPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(lineaPedidoService.getLineaPedidoById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public LineaPedidoDTO createLineaPedido(@Valid @RequestBody LineaPedidoDTOCrear dto) {
        return lineaPedidoService.createLineaPedido(dto);
    }
}
