package es.safareyes.coffestation.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "descuento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Descuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Column(name = "origen", nullable = false, length = 50)
    private String origen;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;


}
