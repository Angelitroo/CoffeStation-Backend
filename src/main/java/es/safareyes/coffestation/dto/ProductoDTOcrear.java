package es.safareyes.coffestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTOcrear {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal iva;
    private Boolean disponible;
    private Boolean activo;
    private Long categoriaId;
    private List<Long> alergenosIds;
}
