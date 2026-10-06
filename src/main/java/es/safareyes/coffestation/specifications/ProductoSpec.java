package es.safareyes.coffestation.specifications;

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
            if(nombre != null && !nombre.isBlank()){
                filtros.add(cb.like(cb.lower(root.get("nombre")),
                        "%" + nombre.toLowerCase() + "%"));
            }

            //Busqueda por nombre categoria
            if(categoriaNombre != null && !categoriaNombre.isBlank()){
                Join<Producto, Categoria> categoriaJoin = root.join("categoria", JoinType.INNER);
                filtros.add(cb.like(cb.lower(categoriaJoin.get("nombre")),
                        "%" + categoriaNombre.toLowerCase() + "%"));
            }

            //Busqueda por disponibilidad
            if(disponible != null){
                filtros.add(cb.equal(root.get("disponible"), disponible));
            }

            //Busqueda por alergeno
            if(nombreAlergeno != null && !nombreAlergeno.isBlank() && conAlergeno != null){
                Join<Producto, Alergeno> alergenoJoin = root.join("alergeno",
                        conAlergeno ? JoinType.INNER : JoinType.LEFT);
                filtros.add(cb.like(cb.lower(alergenoJoin.get("nombre")),
                        "%" + nombreAlergeno.toLowerCase() + "%"));

                if(!conAlergeno){
                    filtros.add(cb.isNull(alergenoJoin.get("id")));
                }
            }

            //Busqueda por rango precio
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
