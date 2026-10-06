package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120)
        @Schema(example = "José Martínez")
        String nombre,

        @NotBlank(message = "El NIT es obligatorio (use CF si no tiene)")
        @Size(max = 20)
        @Schema(example = "1234567-8")
        String nit,

        @Size(max = 20)
        @Schema(example = "5555-6666")
        String telefono,

        @Email(message = "El email no tiene un formato válido")
        @Size(max = 120)
        @Schema(example = "jose@gmail.com")
        String email,

        @Size(max = 255)
        @Schema(example = "Zona 1, Ciudad")
        String direccion
) {
}
