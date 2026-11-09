package es.safareyes.coffestation.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistroDTO(
        @NotBlank String nombre,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String dni
) {}
