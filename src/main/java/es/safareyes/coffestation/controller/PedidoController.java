package es.safareyes.coffestation.controller;
import es.safareyes.coffestation.dto.PedidoDTO;
import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public Page<Pedido> getAllPedidos(
            @RequestParam(required = false) LocalDateTime fecha,
            @RequestParam(required = false) Estado estado,
            Pageable pageable)
    {

        return pedidoService.getAllPedidosFiltros(fecha, estado, pageable);
    }

    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable Long id){
        return pedidoService.getPedidoById(id);
    }

    @GetMapping("/cupon/{codigo}")
    public Integer getPedidosByCodigo(@PathVariable String codigo){
        return pedidoService.getPedidosByCupon(codigo);
    }

    @PutMapping("/actualizar/{id}")
    public Pedido updatePedidoEstado(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO){
        return pedidoService.updatePedidoEstado(id, pedidoDTO);
    }

}
