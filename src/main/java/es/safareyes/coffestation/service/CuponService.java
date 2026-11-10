package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.CuponDTO;
import es.safareyes.coffestation.dto.CuponValidarDTO;
import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.exceptions.ReglaNegocioException;
import es.safareyes.coffestation.model.Cliente;
import es.safareyes.coffestation.model.Cupon;
import es.safareyes.coffestation.repository.ClienteRepository;
import es.safareyes.coffestation.repository.CuponRepository;
import es.safareyes.coffestation.repository.PedidoRepository;
import es.safareyes.coffestation.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class CuponService {

    private CuponRepository cuponRepository;

    private ClienteRepository clienteRepository;

    private JwtService jwtService;

    public List<CuponDTO> getAllCupones() {
        return cuponRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public CuponDTO getCuponById(Long id) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cupón no encontrado: " + id));
        return convertToDTO(cupon);
    }

    public CuponDTO getCuponByCodigo(String codigo) {
        Cupon cupon = cuponRepository.findCuponByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cupón no encontrado: " + codigo));
        return convertToDTO(cupon);
    }

    public Integer getUsosCupon(String codigo){
        return cuponRepository.findUsosCupon(codigo);
    }

    public CuponDTO createCupon(CuponDTO dto) {
        Cupon cupon = new Cupon();
        cupon.setCodigo(dto.codigo());
        cupon.setTipo(dto.tipo());
        cupon.setValor(dto.valor());
        cupon.setFechaInicio(dto.fechaInicio());
        cupon.setFechaFin(dto.fechaFin());
        cupon.setMaxUsos(dto.maxUsos());
        cupon.setImporteMinimo(dto.importeMinimo());

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + dto.clienteId() + " no existe"));
        cupon.setCliente(cliente);

        return convertToDTO(cuponRepository.save(cupon));
    }

    /*
    Un cupón es válido si existe, la fecha actual está dentro de su
    vigencia,no ha alcanzado sus usos máximos, el subtotal supera su importe mínimo y,
    si es personal,el email coincide con el del cliente.
    */
    //A traves de cupon.getidCliente --> compara ese email con el de dentro de la aplicación, a través de token ejem
    //Validar permite que se use, es una comprobación, validar=canjear
    public CuponValidarDTO validarCupon(String codigo, BigDecimal subtotal) {
        // Existe?
        Cupon cupon = cuponRepository.findCuponByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cupón no encontrado: " + codigo));

        // fecha actual está dentro de vigencia
        if (LocalDateTime.now().isBefore(cupon.getFechaInicio())) {
            throw new ReglaNegocioException("El cupón todavía no está activo");
        }
        if (LocalDateTime.now().isAfter(cupon.getFechaFin())) {
            throw new ReglaNegocioException("El cupón ha caducado");
        }

        // Usos máximos
        Integer usos = cuponRepository.findUsosCupon(codigo);
        if (usos >= cupon.getMaxUsos()) {
            throw new ReglaNegocioException("El cupón ha alcanzado su número máximo de usos");
        }

        //subtotal supera su importe mínimo
        if (cupon.getImporteMinimo() != null && subtotal.compareTo(cupon.getImporteMinimo()) < 0) {
            throw new ReglaNegocioException("El subtotal no alcanza el importe mínimo de " + cupon.getImporteMinimo() + " €");
        }

        //email coincide con el del cliente
        if (cupon.getCliente() != null) {
            Long idUsuarioActual = jwtService.getUsuarioActual().getId();
            Long idUsuarioCupon = cupon.getCliente().getUsuario().getId();
            if (!idUsuarioCupon.equals(idUsuarioActual)) {
                throw new ReglaNegocioException("Este cupón no pertenece al cliente actual");
            }
        }


        return new CuponValidarDTO(codigo, cupon.getFechaFin(), cupon.getMaxUsos());

    }

    public CuponDTO convertToDTO(Cupon cupon) {
        return new CuponDTO(
                cupon.getCodigo(),
                cupon.getTipo(),
                cupon.getValor(),
                cupon.getFechaInicio(),
                cupon.getFechaFin(),
                cupon.getMaxUsos(),
                cupon.getImporteMinimo(),
                cupon.getCliente() != null ? cupon.getCliente().getId() : null
        );
    }

}
