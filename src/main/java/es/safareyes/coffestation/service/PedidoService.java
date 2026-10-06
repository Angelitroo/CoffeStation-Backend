package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.PedidoDTO;
import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.model.Cupon;
import es.safareyes.coffestation.model.Descuento;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.repository.CuponRepository;
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

    @Autowired
    private CuponRepository cuponRepository;


    public Page<Pedido> getAllPedidosFiltros(LocalDateTime fecha, Estado estado, Pageable pageable){
        return pedidoRepository.findAll(
                PedidoSpec.filtrosPedidos(fecha, estado), pageable);
    }

    public Pedido getPedidoById(Long id){
        return pedidoRepository.findById(id).orElse(null);
    }

    public PedidoDTO createPedido(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        pedido.setId(pedidoDTO.getId());
        pedido.setFecha(LocalDateTime.now());
        pedido.setNumeroTurno(pedidoDTO.getNumeroTurno());
        pedido.setEstado(pedidoDTO.getEstado());

        //Primero vemos si se usa cupon o no
        if (pedidoDTO.getCuponId() != null) {
            Cupon cupon = cuponRepository.findById(pedidoDTO.getCuponId())
                    .orElseThrow(() -> new IllegalArgumentException("Cupon no encontrado"));
            //En caso de usar un cupon se le resta un uso maximo al cupon
            cupon.setMaxUsos(cupon.getMaxUsos() - 1);
            cuponRepository.save(cupon);
            pedido.setCupon(cupon);
        }

        Pedido savedPedido = pedidoRepository.save(pedido);
        return convertToDTO(savedPedido);
    }


    public Pedido updatePedidoEstado(Long id, Estado estado) {
        //Cogemos el pedido que queremos actualizar
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        //Cogemos el nuevo valor del estado y seteamos
        pedido.setEstado(estado);

        //Cancelar un pedido devuelve el uso al cupón aplicado.
        if (estado == Estado.CANCELADO && pedido.getCupon() != null) {
            Cupon cupon = pedido.getCupon();
            /*
            Le sumamos el numero de usos a los que le quedaban:
            Explicacion:Por ejemplo teniamos 10 usos maximos y hemos usado 3 lo que lo dejaria en 7,
            pero como no sabemos el numero original lo que hacemos es "restaurar" el numero original
             */

            Integer usos = pedidoRepository.findByCupon_Codigo(cupon.getCodigo());
            cupon.setMaxUsos(cupon.getMaxUsos() + usos);
        }
        return pedidoRepository.save(pedido);
    }

    public Integer getUsosCupon(String codigo){
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

    public PedidoDTO convertToDTO(Pedido pedido){
        return new PedidoDTO(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getNumeroTurno(),
                pedido.getEstado(),
                pedido.getCupon() != null ? pedido.getCupon().getId() : null
        );
    }

}
