package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.ProductoDTO;
import es.safareyes.coffestation.dto.ProductoDTOcrear;
import es.safareyes.coffestation.model.Producto;
import es.safareyes.coffestation.service.ProductoService;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/productos")
@AllArgsConstructor
@Validated
public class ProductoController {

    private ProductoService productoService;

    @GetMapping()
    public Page<ProductoDTO> getAllProductosFiltros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) String alergeno,
            @RequestParam(required = false) Boolean conAlergeno,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Pageable pageable) {

        return productoService.getAllProductosFiltros(nombre, categoria, disponible,
                alergeno, conAlergeno, precioMin, precioMax, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id){
        return ResponseEntity.ok(productoService.getProductoById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<ProductoDTO> createProducto(@Valid @RequestBody ProductoDTOcrear productoDTOcrear){
        return ResponseEntity.ok(productoService.createProducto(productoDTOcrear));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO){
        return ResponseEntity.ok(productoService.updateProducto(productoDTO, id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','BARISTA')")
    @PatchMapping("/actualizar/disponibilidad/{id}")
    public ResponseEntity<ProductoDTO> updateProductoDisponibilidad(@PathVariable Long id, @NotNull @RequestBody Boolean disponibilidad){
        return ResponseEntity.ok(productoService.updateProductoDisponibilidad(disponibilidad, id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id){
        try {
            return ResponseEntity.ok(productoService.deleteProductoById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
