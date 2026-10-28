package es.safareyes.coffestation.model;


import es.safareyes.coffestation.enums.Tipo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "valor", nullable = false, precision = 6, scale = 2)
    private BigDecimal valor;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "max_usos", nullable = false)
    private Integer maxUsos;

    @Column(name = "importe_minimo", precision = 8, scale = 2)
    private BigDecimal importeMinimo;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

}
