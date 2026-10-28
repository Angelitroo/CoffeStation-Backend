package es.safareyes.coffestation.specifications;

import es.safareyes.coffestation.dto.ProductoDTO;
import es.safareyes.coffestation.model.Alergeno;
import es.safareyes.coffestation.model.Categoria;
import es.safareyes.coffestation.model.Producto;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ProductoSpec {
    private ProductoSpec(){}

    //Busqueda con filtros dinamicos
    public static Specification<Producto> filtrosProductos(String nombre, String categoriaNombre,
                                                              Boolean disponible, String nombreAlergeno, Boolean conAlergeno,
                                                              BigDecimal precioMinimo, BigDecimal precioMaximo)
    {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            //Busqueda por nombre
            //http://localhost:8080/productos?nombre=café
            if(nombre != null && !nombre.isBlank()){
                filtros.add(cb.like(cb.lower(root.get("nombre")),
                        "%" + nombre.toLowerCase() + "%"));
            }

            //Busqueda por nombre categoria
            //http://localhost:8080/productos?categoria=bebidas
            if(categoriaNombre != null && !categoriaNombre.isBlank()){
                Join<Producto, Categoria> categoriaJoin = root.join("categoria", JoinType.INNER);
                filtros.add(cb.like(cb.lower(categoriaJoin.get("nombre")),
                        "%" + categoriaNombre.toLowerCase() + "%"));
            }

            //Busqueda por disponibilidad true/false
            //http://localhost:8080/productos?disponible=true
            if(disponible != null){
                filtros.add(cb.equal(root.get("disponible"), disponible));
            }

            //Busqueda por alergeno los que tienen o no lo tienen
            //http://localhost:8080/productos?alergeno=gluten&conAlergeno=true
            if(nombreAlergeno != null && !nombreAlergeno.isBlank() && conAlergeno != null){
                if(conAlergeno){
                    Join<Producto, Alergeno> alergenoJoin = root.join("alergeno", JoinType.INNER);
                    filtros.add(cb.like(cb.lower(alergenoJoin.get("nombre")),
                            "%" + nombreAlergeno.toLowerCase() + "%"));
                } else {
                    Join<Producto, Alergeno> alergenoJoin = root.join("alergeno", JoinType.LEFT);
                    Predicate nombreMatch = cb.like(cb.lower(alergenoJoin.get("nombre")),
                            "%" + nombreAlergeno.toLowerCase() + "%");
                    filtros.add(cb.or(
                            cb.isNull(alergenoJoin.get("id")),
                            cb.not(nombreMatch)
                    ));
                }
            }

            //Busqueda por rango precio
            //http://localhost:8080/productos?precioMin=5.00
            //http://localhost:8080/productos?precioMax=10.00
            //http://localhost:8080/productos?precioMin=5.00&precioMax=15.00
            if(precioMinimo != null){
                filtros.add(cb.ge(root.get("precio"), precioMinimo));
            }
            if(precioMaximo != null){
                filtros.add(cb.le(root.get("precio"), precioMaximo));
            }


            if(filtros.isEmpty()) return null;

            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}
