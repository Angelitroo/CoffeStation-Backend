package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.PedidoDTO;
import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.exceptions.ReglaNegocioException;
import es.safareyes.coffestation.model.Cupon;
import es.safareyes.coffestation.model.Descuento;
import es.safareyes.coffestation.model.LineaPedido;
import es.safareyes.coffestation.model.Pedido;
import es.safareyes.coffestation.repository.CuponRepository;
import es.safareyes.coffestation.repository.DescuentoRepository;
import es.safareyes.coffestation.repository.LineaPedidoRepository;
import es.safareyes.coffestation.repository.PedidoRepository;
import es.safareyes.coffestation.specifications.PedidoSpec;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class PedidoService {

    private PedidoRepository pedidoRepository;

    private DescuentoRepository descuentoRepository;

    private LineaPedidoRepository lineaPedidoRepository;

    private CuponRepository cuponRepository;


    public Page<PedidoDTO> getAllPedidosFiltros(LocalDateTime fecha, Estado estado, Pageable pageable){
        return pedidoRepository.findAll(
                PedidoSpec.filtrosPedidos(fecha, estado), pageable).map(this::convertToDTO);
    }

    public Pedido getPedidoById(Long id){
        return pedidoRepository.findById(id).orElse(null);
    }

    ////////////////////////////////////////////////////////////////////////
    //SIGUIENTE SESION:
    //EL pedido esta formado por una serie de lineas de pedido, de esas lineas sacar los datos para caluclar
    //totales subtotales etc, es la suma de esas lineapedidos que forman en pedido
    public PedidoDTO createPedido(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(pedidoDTO.estado());
        pedido.setBaseImponible(pedidoDTO.baseImponible());
        pedido.setSubtotal(pedidoDTO.subtotal());
        pedido.setTotal(pedidoDTO.total());

        //El número de turno es el número de pedidos creados ese día más uno.
        Long numeroPedidosL = pedidoRepository.getPedidosByFecha(LocalDate.now());

        //Convierto de Long a Integer
        Integer numeroPedidosI = numeroPedidosL.intValue();
        pedido.setNumeroTurno(numeroPedidosI + 1);

        //Primero vemos si se usa cupon o no
        if (pedidoDTO.cuponId() != null) {
            Cupon cupon = cuponRepository.findById(pedidoDTO.cuponId())
                    .orElseThrow(() -> new IllegalArgumentException("Cupon no encontrado"));
            //En caso de usar un cupon se le resta un uso maximo al cupon
            cupon.setMaxUsos(cupon.getMaxUsos() - 1);
            cuponRepository.save(cupon);
            pedido.setCupon(cupon);
        }

        Pedido savedPedido = pedidoRepository.save(pedido);
        return convertToDTO(savedPedido);
    }


    public Pedido updatePedidoEstado(Long id, Estado estadoNuevo) {
        //Cogemos el pedido que queremos actualizar
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        //Transiciones válidas: PENDIENTE → EN_PREPARACION → LISTO → ENTREGADO y PENDIENTE → CANCELADO .
        //Cualquier otra devuelve 422
        Estado actual = pedido.getEstado();
        boolean valida = switch (actual) {
            case PENDIENTE      -> estadoNuevo == Estado.EN_PREPARACION || estadoNuevo == Estado.CANCELADO;
            case EN_PREPARACION -> estadoNuevo == Estado.LISTO;
            case LISTO          -> estadoNuevo == Estado.ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
        if (!valida) {
            throw new ReglaNegocioException("Transición no permitida: " + actual + " → " + estadoNuevo);
        }
        //Cogemos el nuevo valor del estado y seteamos
        pedido.setEstado(estadoNuevo);

        //Cancelar un pedido devuelve el uso al cupón aplicado.
        if (estadoNuevo == Estado.CANCELADO && pedido.getCupon() != null) {
            Cupon cupon = pedido.getCupon();
            /*
            Le sumamos el numero de usos a los que le quedaban:
            Explicacion:Por ejemplo teniamos 10 usos maximos y hemos usado 3 lo que lo dejaria en 7,
            pero como no sabemos el numero original lo que hacemos es "restaurar" el numero original
             */

            Integer usos = cuponRepository.findUsosCupon(cupon.getCodigo());
            cupon.setMaxUsos(cupon.getMaxUsos() + usos);
        }
        return pedidoRepository.save(pedido);
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
                pedido.getFecha(),
                pedido.getNumeroTurno(),
                pedido.getBaseImponible(),
                pedido.getSubtotal(),
                pedido.getTotal(),
                pedido.getEstado(),
                pedido.getCupon() != null ? pedido.getCupon().getId() : null
        );
    }

}
