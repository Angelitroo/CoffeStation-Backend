package es.safareyes.coffestation.specifications;

import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.model.Pedido;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public final class PedidoSpec {
    private PedidoSpec(){}

    //Busqueda filtros dinamicos
    public static Specification<Pedido> filtrosPedidos(LocalDateTime fecha, Estado estado){

        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            //Busqueda por fecha
            if(fecha != null){
                filtros.add(cb.equal(root.get("fecha"), fecha));
            }

            //Busqueda por estado
            if(estado != null){
                filtros.add(cb.equal(root.get("estado"), estado));
            }

            if(filtros.isEmpty()) return null;

            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}
