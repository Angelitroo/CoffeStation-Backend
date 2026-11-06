package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.CuponDTO;
import es.safareyes.coffestation.dto.CuponValidarDTO;
import es.safareyes.coffestation.model.Cupon;
import es.safareyes.coffestation.service.CuponService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cupones")
@AllArgsConstructor
public class CuponController {

    private CuponService cuponService;

    @GetMapping
    public List<CuponDTO> getAllCupones(){
        return cuponService.getAllCupones();
    }


    @GetMapping("/{id}")
    public CuponDTO getCuponById(@PathVariable Long id){
        return cuponService.getCuponById(id);
    }

    @GetMapping("/codigo/{codigo}")
    public CuponDTO getCuponByCodigo(@PathVariable String codigo){
        return cuponService.getCuponByCodigo(codigo);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public CuponDTO createCupon(@Valid @RequestBody CuponDTO cuponDTO){
        return cuponService.createCupon(cuponDTO);
    }

}
