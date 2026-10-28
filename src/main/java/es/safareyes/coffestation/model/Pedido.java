package es.safareyes.coffestation.model;


import es.safareyes.coffestation.enums.Estado;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "numero_turno", nullable = false)
    private Integer numeroTurno;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_cupon")
    private Cupon cupon;

}
