package es.safareyes.coffestation.service;

import es.safareyes.coffestation.model.Categoria;
import es.safareyes.coffestation.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategorias(){
        return categoriaRepository.findAll();
    }

    public List<Categoria> getAllCategoriasConProductosActivos(){
        return categoriaRepository.findCategoriasConProductosActivos();
    }
}
