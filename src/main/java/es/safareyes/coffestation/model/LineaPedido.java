package es.safareyes.coffestation.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "linea_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_sin_iva", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioSinIva;

    @Column(name = "iva", nullable = false, precision = 5, scale = 2)
    private BigDecimal iva;

    @Column(name = "total", nullable = false, precision = 8, scale = 2)
    private BigDecimal total;


}
