package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.model.Categoria;
import es.safareyes.coffestation.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias(){
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> getAllCategoriasConProductosActivos(){
        return ResponseEntity.ok(categoriaService.getAllCategoriasConProductosActivos());
    }
}
