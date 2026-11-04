package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.ProductoDTO;
import es.safareyes.coffestation.dto.ProductoDTOcrear;
import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.exceptions.ReglaNegocioException;
import es.safareyes.coffestation.model.Alergeno;
import es.safareyes.coffestation.model.Categoria;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Producto;
import es.safareyes.coffestation.repository.AlergenoRepository;
import es.safareyes.coffestation.repository.CategoriaRepository;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import es.safareyes.coffestation.repository.ProductoRepository;
import es.safareyes.coffestation.specifications.ProductoSpec;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static  es.safareyes.coffestation.specifications.ProductoSpec.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class ProductoService {

    private ProductoRepository productoRepository;

    private CategoriaRepository categoriaRepository;

    private AlergenoRepository alergenoRepository;

    private LineaPedidoRepository lineaPedidoRepository;

    public Page<ProductoDTO> getAllProductosFiltros(String nombre, String categoriaNombre,
                                                 Boolean disponible, String nombreAlergeno, Boolean conAlergeno,
                                                 BigDecimal precioMinimo, BigDecimal precioMaximo, Pageable pageable) {

        Page<Producto> productos = productoRepository.findAll(
                ProductoSpec.filtrosProductos(nombre, categoriaNombre, disponible,
                        nombreAlergeno, conAlergeno, precioMinimo, precioMaximo),
                pageable
        );
        return productos.map(this::convertToDTO);
    }


    public Producto getProductoById(Long id){
        //Falta que devuelva el 422
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto " + id + " no existe"));

        if (!p.getActivo()) {
            throw new ReglaNegocioException("Producto " + p.getId() + " (" + p.getNombre() + ") está dado de baja");
        }
        if (!p.getDisponible()) {
            throw new ReglaNegocioException("Producto " + p.getId() + " (" + p.getNombre() + ") está agotado");
        }

        return p;
    }

    public ProductoDTO createProducto(ProductoDTOcrear productoDTO){
        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecioSinIva(productoDTO.getPrecio());
        producto.setIva(productoDTO.getIva());
        producto.setDisponible(productoDTO.getDisponible());
        producto.setActivo(productoDTO.getActivo());

        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada"));
        producto.setCategoria(categoria);

        List<Alergeno> alergenos = alergenoRepository.findAllById(productoDTO.getAlergenosIds());
        if (alergenos.size() != productoDTO.getAlergenosIds().size()) {
            throw new IllegalArgumentException("Uno o más IDs de alérgenos no son válidos");
        }
        producto.setAlergeno(alergenos);

        Producto savedProducto = productoRepository.save(producto);
        return convertToDTO(savedProducto);
    }

    public ProductoDTO updateProducto(ProductoDTO productoDTO, Long id){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecioSinIva(productoDTO.getPrecioSinIva());
        producto.setIva(productoDTO.getIva());
        producto.setDisponible(productoDTO.getDisponible());
        producto.setActivo(productoDTO.getActivo());

        Producto productoUpdated = productoRepository.save(producto);
        return convertToDTO(productoUpdated);
    }

    public ProductoDTO updateProductoDisponibilidad(Boolean disponible, Long id){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        producto.setDisponible(disponible);

        Producto productoUpdated = productoRepository.save(producto);
        return convertToDTO(productoUpdated);
    }

    public String deleteProductoById(Long id){
        List<LineaPedido> lineas = lineaPedidoRepository.findAllByProductoId(id);
        lineaPedidoRepository.deleteAll(lineas);
        productoRepository.deleteById(id);
        return "Producto Eliminado";
    }



    public ProductoDTO convertToDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioSinIva(),
                producto.getIva(),
                producto.getDisponible(),
                producto.getActivo()
        );
    }
}
