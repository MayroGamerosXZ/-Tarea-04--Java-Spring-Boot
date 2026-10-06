package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede tener más de 80 caracteres")
        @Schema(example = "Herramientas Manuales")
        String nombre,

        @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
        @Schema(example = "Martillos, destornilladores, alicates, etc.")
        String descripcion
) {
}
