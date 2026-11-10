package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {
    Cliente findClienteByUsuarioId(Long idUsuario);
}
