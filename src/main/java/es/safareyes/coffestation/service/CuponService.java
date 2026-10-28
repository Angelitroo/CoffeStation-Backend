package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.CuponValidarDTO;
import es.safareyes.coffestation.model.Cupon;
import es.safareyes.coffestation.repository.CuponRepository;
import es.safareyes.coffestation.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class CuponService {
    @Autowired
    private CuponRepository cuponRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Cupon> getAllCupones() {
        return cuponRepository.findAll();
    }

    public Cupon getCuponById(Long id) {
        return cuponRepository.findById(id).orElse(null);
    }

    /*
    Un cupón es válido si existe, la fecha actual está dentro de su
    vigencia,no ha alcanzado sus usos máximos, el subtotal supera su importe mínimo y,
    si es personal,el email coincide con el del cliente.
    */
    public CuponValidarDTO validarCupon(String codigo) {
        Cupon cupon = cuponRepository.findCuponByCodigo(codigo);
        CuponValidarDTO cuponValidarDTO = new CuponValidarDTO();
        cuponValidarDTO.setCodigo(codigo);
        cuponValidarDTO.setMaxUsos(cupon.getMaxUsos());
        cuponValidarDTO.setFechaFin(cupon.getFechaFin());

        Integer usos = pedidoRepository.findUsosCupon(codigo);

        if (codigo != null &&
                cuponValidarDTO.getMaxUsos() > usos &&
                cuponValidarDTO.getFechaFin().isAfter(LocalDateTime.now()))
        {

        }

        return cuponValidarDTO;
    }
}
