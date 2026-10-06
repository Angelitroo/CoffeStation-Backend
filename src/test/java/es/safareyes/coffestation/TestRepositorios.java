package es.safareyes.coffestation;

import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.model.Alergeno;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.model.Producto;
import es.safareyes.coffestation.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import es.safareyes.coffestation.model.*;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestRepositorios {

    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AlergenoRepository alergenoRepository;

    @Test
    void testTabla(){
        List<Alergeno> alergeno = alergenoRepository.findAll();
        alergeno.forEach(System.out::println);
    }
}
