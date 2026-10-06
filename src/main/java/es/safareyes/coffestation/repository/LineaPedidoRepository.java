package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LineaPedidoRepository extends JpaRepository <LineaPedido, Long> {

    //Resumen del dia
    @Query("""
    SELECT new es.safareyes.coffestation.dto.ResumenDiaDTO(
            /*Fecha*/         CAST(:fecha AS LocalDate),
            /*Numero pedidos*/COUNT(DISTINCT p.id),
            /*Precio con IVA*/COALESCE(SUM(l.cantidad * l.precioUnitario), 0),
            /*Precio sin IVA*/COALESCE(SUM(l.cantidad * pr.precio), 0)
        )
        FROM LineaPedido l
        JOIN l.pedido p
        JOIN l.producto pr
        WHERE CAST(p.fecha AS LocalDate) = :fecha

""")
    ResumenDiaDTO getResumenDiaByFecha(LocalDate fecha);

    //Ranking de productos más vendidos
    @Query(value = """
            SELECT p.* FROM producto p
            JOIN linea_pedido lp ON p.id = lp.id_producto
            GROUP BY p.id
            ORDER BY SUM(lp.cantidad) DESC
            """,
            nativeQuery = true)
    Page<Producto> findProductosMasVendidos(Pageable pageable);
}
