package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CalculoAreaRequest(
        @NotNull(message = "El largo es obligatorio")
        @Min(value = 1, message = "El largo debe ser mayor a 0")
        @Schema(example = "10")
        Double largo,

        @NotNull(message = "El ancho es obligatorio")
        @Min(value = 1, message = "El ancho debe ser mayor a 0")
        @Schema(example = "4")
        Double ancho
) {
        public Double area() {
                return largo * ancho;
        }
}
