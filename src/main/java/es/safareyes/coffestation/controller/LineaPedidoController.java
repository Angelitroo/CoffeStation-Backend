package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.service.LineaPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/informe")
public class LineaPedidoController {
    @Autowired
    private LineaPedidoService lineaPedidoService;

    @GetMapping("/resumen/{fecha}")
    public ResponseEntity<ResumenDiaDTO> getResumenDiaByFecha(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(lineaPedidoService.getResumenDiaByFecha(fecha));
    }
}
