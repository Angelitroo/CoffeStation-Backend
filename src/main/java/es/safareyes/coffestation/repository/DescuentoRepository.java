package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescuentoRepository extends JpaRepository <Descuento, Long> {
    //Para eliminar pedido, relacion descuento
    List<Descuento> findAllByPedidoId(Long id);
}
