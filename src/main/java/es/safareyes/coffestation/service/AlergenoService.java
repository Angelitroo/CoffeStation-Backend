package es.safareyes.coffestation.service;

import es.safareyes.coffestation.model.Alergeno;
import es.safareyes.coffestation.repository.AlergenoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class AlergenoService {

    private AlergenoRepository alergenoRepository;

    public List<Alergeno> getAllAlergeno(){
        return alergenoRepository.findAll();
    }
}
