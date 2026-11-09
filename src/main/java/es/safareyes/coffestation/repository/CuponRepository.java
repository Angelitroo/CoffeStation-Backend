package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository <Cupon, Long> {
    Optional<Cupon> findCuponByCodigo(String codigo);

    //Saco el numero de usos
    @Query("select count(p) from Pedido p where p.cupon.codigo = :codigo")
    Integer findUsosCupon(String codigo);
}
