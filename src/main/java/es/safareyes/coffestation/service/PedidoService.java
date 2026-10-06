package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.PedidoDTO;
import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.model.Descuento;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.repository.DescuentoRepository;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import es.safareyes.coffestation.repository.PedidoRepository;
import es.safareyes.coffestation.specifications.PedidoSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;

    public Page<Pedido> getAllPedidosFiltros(LocalDateTime fecha, Estado estado, Pageable pageable){
        return pedidoRepository.findAll(
                PedidoSpec.filtrosPedidos(fecha, estado), pageable);
    }

    public Pedido getPedidoById(Long id){
        return pedidoRepository.findById(id).orElse(null);
    }

    public Pedido updatePedidoEstado(Long id, PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        Estado estadoNuevo = pedidoDTO.getEstado();
        pedido.setEstado(estadoNuevo);
        //Cancelar un pedido devuelve el uso al cupón aplicado.
        if (estadoNuevo == Estado.CANCELADO && pedido.getCupon() != null) {
            pedido.getCupon().setMaxUsos(pedidoDTO.getMaxUsos());
        }
        return pedidoRepository.save(pedido);
    }

    public Integer getPedidosByCupon(String codigo){
        return pedidoRepository.findByCupon_Codigo(codigo);

    }

    public String deletePedidoById(Long id){
        List<Descuento> descuentos = descuentoRepository.findAllByPedidoId(id);
        descuentoRepository.deleteAll(descuentos);

        List<LineaPedido> lineas = lineaPedidoRepository.findAllByPedidoId(id);
        lineaPedidoRepository.deleteAll(lineas);

        pedidoRepository.deleteById(id);
        return "Pedido Eliminado";
    }

}
