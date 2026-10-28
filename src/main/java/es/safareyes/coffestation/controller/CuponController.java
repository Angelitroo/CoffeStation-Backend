package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.CuponValidarDTO;
import es.safareyes.coffestation.model.Cupon;
import es.safareyes.coffestation.service.CuponService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cupones")
@AllArgsConstructor
public class CuponController {

    private CuponService cuponService;

    @GetMapping
    public List<Cupon> getAllCupones(){
        return cuponService.getAllCupones();
    }

    @GetMapping("{id}")
    public Cupon getCuponById(@PathVariable Long id){
        return cuponService.getCuponById(id);
    }

}
