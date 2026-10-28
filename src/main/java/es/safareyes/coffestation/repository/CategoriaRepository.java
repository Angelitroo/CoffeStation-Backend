package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository <Categoria, Long> {

    @Query(value = "SELECT c.* FROM categoria c " +
            "WHERE c.id IN (SELECT DISTINCT p.id_categoria FROM producto p WHERE p.activo = true)",
            nativeQuery = true)
    List<Categoria> findCategoriasConProductosActivos();

}