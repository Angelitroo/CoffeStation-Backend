package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository <Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    //Saco el numero de usos
    @Query("select count(p) from Pedido p where p.cupon.codigo = :codigo")
    Integer findUsosCupon(String codigo);

    //Cuento numero de pedidos creados en un dia, independientemente de detalles
    @Query("SELECT COUNT(p) FROM Pedido p WHERE CAST(p.fecha AS LocalDate) = :fecha")
    Long getPedidosByFecha(LocalDate fecha);

    /*
    //Listado paginado por estado y fecha; un cliente solo ve los suyos
    Page<Pedido> findAllByOrderByEstadoAscFechaDesc(Pageable pageable);


     */

}

