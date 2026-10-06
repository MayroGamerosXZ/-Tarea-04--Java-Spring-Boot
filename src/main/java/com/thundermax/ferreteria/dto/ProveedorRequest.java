package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProveedorRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120)
        @Schema(example = "Distribuidora El Martillo, S.A.")
        String nombre,

        @Size(max = 20)
        @Schema(example = "1234567-8")
        String nit,

        @Size(max = 100)
        @Schema(example = "Juan Pérez")
        String contacto,

        @Size(max = 20)
        @Schema(example = "2233-4455")
        String telefono,

        @Email(message = "El email no tiene un formato válido")
        @Size(max = 120)
        @Schema(example = "ventas@elmartillo.com.gt")
        String email,

        @Size(max = 255)
        @Schema(example = "Zona 12, Ciudad de Guatemala")
        String direccion
) {
}
