package es.safareyes.coffestation.repository;

import es.safareyes.coffestation.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository <Categoria, Long> {

    //Categorías con número de productos activos
    @Query(value = "SELECT DISTINCT c.* FROM categoria c " +
            "INNER JOIN producto p ON c.id = p.id_categoria " +
            "WHERE p.activo = true", nativeQuery = true)
    List<Categoria> findCategoriasConProductosActivos();

}