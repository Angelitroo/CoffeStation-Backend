package es.safareyes.coffestation.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "dni", nullable = false, unique = true, length = 9)
    private String dni;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


}
