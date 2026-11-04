package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.dto.ProductoRankingDTO;
import es.safareyes.coffestation.dto.ResumenDiaDTO;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LineaPedidoRepository extends JpaRepository <LineaPedido, Long> {

    //Los informes de ventas solo cuentan pedidos ENTREGADO .

    //Resumen del dia
    //http://localhost:8080/informe/resumen/2026-09-28
    @Query("""
    SELECT new es.safareyes.coffestation.dto.ResumenDiaDTO(
            /*Fecha*/         CAST(:fecha AS LocalDate),
            /*Numero pedidos*/COUNT(DISTINCT p.id),
            /*Precio con IVA*/COALESCE(SUM(l.cantidad * l.precioSinIva), 0),
            /*Precio sin IVA*/COALESCE(SUM(l.cantidad * l.precioSinIva) * l.iva, 0)
        )
        FROM LineaPedido l
        JOIN l.pedido p
        JOIN l.producto pr
        WHERE CAST(p.fecha AS LocalDate) = :fecha
            AND p.estado = 'ENTREGADO'

""")
    ResumenDiaDTO getResumenDiaByFecha(LocalDate fecha);

    //Ranking de productos más vendidos
    //http://localhost:8080/informe/ranking
    @Query("""
            SELECT new es.safareyes.coffestation.dto.ProductoRankingDTO(
                pr.id,
                pr.nombre,
                CAST(SUM(lp.cantidad) AS Integer)
            )
            FROM LineaPedido lp
            JOIN lp.producto pr
            JOIN lp.pedido p
            WHERE CAST(p.fecha AS LocalDate) BETWEEN :fechaMinima AND :fechaMaxima
                AND p.estado = 'ENTREGADO'
                                                                   
            GROUP BY pr.id, pr.nombre
            ORDER BY SUM(lp.cantidad) DESC
""")
    List<ProductoRankingDTO> getProductosMasVendidos(LocalDate fechaMinima, LocalDate fechaMaxima);

    //Para eliminar producto primero eliminar relacion linea_pedido
    List<LineaPedido> findAllByProductoId(Long id);

    //Para eliminar pedido primero eliminar relacion linea_pedido
    List<LineaPedido> findAllByPedidoId(Long id);
}

