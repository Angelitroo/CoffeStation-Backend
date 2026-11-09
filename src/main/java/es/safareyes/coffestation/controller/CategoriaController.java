package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.ProductoDTO;
import es.safareyes.coffestation.model.Categoria;
import es.safareyes.coffestation.service.CategoriaService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@AllArgsConstructor
public class CategoriaController {

    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias(){
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> getAllCategoriasConProductosActivos(){
        return ResponseEntity.ok(categoriaService.getAllCategoriasConProductosActivos());
    }

    @PatchMapping("/actualizar/disponibilidad/{id}")
    public ResponseEntity<List<ProductoDTO>> actualizarDisponibilidad(@PathVariable Long id, @NotNull @RequestBody Boolean disponibilidad){
        return ResponseEntity.ok(categoriaService.actualizarDisponibilidad(disponibilidad, id));
    }


}
