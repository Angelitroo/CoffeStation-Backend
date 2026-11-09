package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.ProductoDTO;
import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.model.Categoria;
import es.safareyes.coffestation.model.Producto;
import es.safareyes.coffestation.repository.CategoriaRepository;
import es.safareyes.coffestation.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class CategoriaService {

    private CategoriaRepository categoriaRepository;

    private ProductoRepository productoRepository;

    private ProductoService productoService;

    public List<Categoria> getAllCategorias(){
        return categoriaRepository.findAll();
    }

    public List<Categoria> getAllCategoriasConProductosActivos(){
        return categoriaRepository.findCategoriasConProductosActivos();
    }


    public List<ProductoDTO> actualizarDisponibilidad(Boolean disponibilidad, Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Categoria " + id + " no existe");
        }
        List<Producto> productos = productoRepository.findByCategoriaId(id);
        productos.forEach(p -> p.setDisponible(disponibilidad));
        return productoRepository.saveAll(productos).stream()
                .map(productoService::convertToDTO)
                .toList();
    }

}
