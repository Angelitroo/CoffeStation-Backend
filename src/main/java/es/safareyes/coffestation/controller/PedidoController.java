package es.safareyes.coffestation.controller;
import es.safareyes.coffestation.dto.PedidoDTO;
import es.safareyes.coffestation.dto.PedidoDTOCrear;
import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
@Validated
public class PedidoController {

    private PedidoService pedidoService;

    @PreAuthorize("hasAnyRole('ADMIN','BARISTA')")
    @GetMapping
    public Page<PedidoDTO> getAllPedidos(
            @RequestParam(required = false) LocalDateTime fecha,
            @RequestParam(required = false) Estado estado,
            Pageable pageable)
    {

        return pedidoService.getAllPedidosFiltros(fecha, estado, pageable);
    }

    //Lo ideal sería comprobar también que el pedido es del cliente que lo pide
    @PreAuthorize("hasAnyRole('ADMIN','BARISTA')")
    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable Long id){
        return pedidoService.getPedidoById(id);
    }


    @PreAuthorize("hasAnyRole('CLIENTE','BARISTA')")
    @PostMapping("/crear")
    public ResponseEntity<PedidoDTO> createPedido(@Valid @RequestBody PedidoDTOCrear pedidoDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.createPedido(pedidoDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','BARISTA')")
    @PatchMapping("/actualizar/estado/{id}")
    public Pedido updatePedidoEstado(@PathVariable Long id, @NotNull @RequestBody Estado estado){
        return pedidoService.updatePedidoEstado(id, estado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<String> deletePedidoById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(pedidoService.deletePedidoById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
