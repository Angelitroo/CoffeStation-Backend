package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.LineaPedidoDTOCrear;
import es.safareyes.coffestation.dto.PedidoDTO;
import es.safareyes.coffestation.dto.PedidoDTOCrear;
import es.safareyes.coffestation.enums.Estado;
import es.safareyes.coffestation.enums.Tipo;
import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.exceptions.ReglaNegocioException;
import es.safareyes.coffestation.model.*;
import es.safareyes.coffestation.repository.*;
import es.safareyes.coffestation.specifications.PedidoSpec;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@AllArgsConstructor
public class PedidoService {

    private PedidoRepository pedidoRepository;

    private DescuentoRepository descuentoRepository;

    private LineaPedidoRepository lineaPedidoRepository;

    private ProductoService productoService;

    private CuponService cuponService;

    private CuponRepository cuponRepository;


    public Page<PedidoDTO> getAllPedidosFiltros(LocalDateTime fecha, Estado estado, Pageable pageable) {
        return pedidoRepository.findAll(
                PedidoSpec.filtrosPedidos(fecha, estado), pageable).map(this::convertToDTO);
    }

    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Transactional
    public PedidoDTO createPedido(PedidoDTOCrear dto) {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(Estado.PENDIENTE);
        //RN-09: turno = pedidos de hoy + 1
        pedido.setNumeroTurno(pedidoRepository.getPedidosByFecha(LocalDate.now()).intValue() + 1);

        //Si el mismo producto llega en varias líneas, se agrupan en una sumando cantidades
        Map<Long, LineaPedidoDTOCrear> agrupadas = new LinkedHashMap<>();
        for (LineaPedidoDTOCrear l : dto.lineas()) {
            agrupadas.merge(l.idProducto(), l, (a, b) -> new LineaPedidoDTOCrear(
                    a.idProducto(),
                    a.cantidad() + b.cantidad(),
                    a.precioSinIva(),
                    a.iva()));
        }

        List<LineaPedido> lineas = new ArrayList<>();
        for (LineaPedidoDTOCrear l : agrupadas.values()) {
            //RN-04: tras agrupar, la línea también debe respetar el máximo de 20 unidades
            if (l.cantidad() > 20) {
                throw new ReglaNegocioException("El producto " + l.idProducto() + " supera las 20 unidades");
            }
            //RN-01: getProductoById ya comprueba que esté activo y disponible
            Producto producto = productoService.getProductoById(l.idProducto());
            //precio sin iva * cantidad
            BigDecimal baseLinea = l.precioSinIva().multiply(BigDecimal.valueOf(l.cantidad()));
            //A esa base se le aplica el iva
            BigDecimal totalLinea = baseLinea
                    .multiply(BigDecimal.ONE.add(l.iva().divide(BigDecimal.valueOf(100))))
                    .setScale(2, RoundingMode.HALF_UP);
            LineaPedido linea = new LineaPedido();
            linea.setPedido(pedido);
            linea.setProducto(producto);
            linea.setCantidad(l.cantidad());
            linea.setPrecioSinIva(l.precioSinIva());
            linea.setIva(l.iva());
            linea.setTotal(totalLinea);
            lineas.add(linea);
        }
        //Una vez creadas todas las líneas, se suman para el pedido
        //donde base imponible es el total sin iva
        pedido.setBaseImponible(lineas.stream()
                .map(lp -> lp.getPrecioSinIva().multiply(BigDecimal.valueOf(lp.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //subtotal es el total con iva
        pedido.setSubtotal(lineas.stream()
                .map(LineaPedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //existe un subtotal y un total, el total es por si se aplica descuento o no
        BigDecimal total = pedido.getSubtotal();
        //El uso del cupon se registra al guardar el pedido con el cupón
        //Si se usa cupón se valida y se aplica el descuento sobre el subtotal -> total
        if (dto.cuponId() != null) {
            Cupon cupon = cuponRepository.findById(dto.cuponId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Cupón no encontrado"));

            //Vigencia, usos, importe mínimo (con el subtotal) y que sea del cliente.
            //Si algo no cumple lanza ReglaNegocioException
            cuponService.validarCupon(cupon.getCodigo(), pedido.getSubtotal());

            BigDecimal descuento = cupon.getTipo() == Tipo.PORCENTAJE
                    ? total.multiply(cupon.getValor()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    : cupon.getValor();
            total = total.subtract(descuento);
            pedido.setCupon(cupon);
        }

        //RN-07: el total nunca puede ser negativo
        pedido.setTotal(total.max(BigDecimal.ZERO));
        Pedido savedPedido = pedidoRepository.save(pedido); //primero el pedido para que tenga id
        lineaPedidoRepository.saveAll(lineas);              //luego sus líneas
        return convertToDTO(savedPedido);
    }


    @Transactional
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
