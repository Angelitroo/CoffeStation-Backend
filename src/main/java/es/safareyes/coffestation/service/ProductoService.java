package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.ProductoDTO;
import es.safareyes.coffestation.dto.ProductoDTOcrear;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

        return productoRepository.findAll(
                ProductoSpec.filtrosProductos(nombre, categoriaNombre, disponible,
                        nombreAlergeno, conAlergeno, precioMinimo, precioMaximo),
                pageable
        ).map(this::convertToDTO);
    }

    public Page<ProductoDTO> getProductosPorNombre(String nombre, Pageable pageable) {
        return productoRepository.findAll(
                ProductoSpec.filtrosProductos(nombre, null, null, null, null, null, null),
                pageable
        ).map(this::convertToDTO);
    }

    public Page<ProductoDTO> getProductosPorCategoria(String categoriaNombre, Pageable pageable) {
        return productoRepository.findAll(
                ProductoSpec.filtrosProductos(null, categoriaNombre, null, null, null, null, null),
                pageable
        ).map(this::convertToDTO);
    }

    public Page<ProductoDTO> getProductosPorDisponibilidad(Boolean disponible, Pageable pageable) {
        return productoRepository.findAll(
                ProductoSpec.filtrosProductos(null, null, disponible, null, null, null, null),
                pageable
        ).map(this::convertToDTO);
    }

    public Page<ProductoDTO> getProductosPorAlergeno(String nombreAlergeno, Boolean conAlergeno, Pageable pageable) {
        return productoRepository.findAll(
                ProductoSpec.filtrosProductos(null, null, null, nombreAlergeno, conAlergeno, null, null),
                pageable
        ).map(this::convertToDTO);
    }

    public Page<ProductoDTO> getProductosPorRangoPrecio(BigDecimal precioMinimo, BigDecimal precioMaximo, Pageable pageable) {
        return productoRepository.findAll(
                ProductoSpec.filtrosProductos(null, null, null, null, null, precioMinimo, precioMaximo),
                pageable
        ).map(this::convertToDTO);
    }

    public Producto getProductoById(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    public ProductoDTO createProducto(ProductoDTOcrear productoDTO){
        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setIva(productoDTO.getIva());
        producto.setDisponible(productoDTO.getDisponible());
        producto.setActivo(productoDTO.getActivo());

        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
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
        producto.setPrecio(productoDTO.getPrecio());
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
                producto.getPrecio(),
                producto.getIva(),
                producto.getDisponible(),
                producto.getActivo()
        );
    }
}
