package es.safareyes.coffestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precioSinIva;
    private BigDecimal iva;
    private Boolean disponible;
    private Boolean activo;
}
