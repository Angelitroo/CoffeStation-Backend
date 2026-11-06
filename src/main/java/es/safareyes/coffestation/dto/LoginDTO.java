package es.safareyes.coffestation.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank String nombre,
        @NotBlank String password
) {}
