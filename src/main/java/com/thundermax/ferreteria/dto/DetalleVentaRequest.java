package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleVentaRequest(
        @NotNull(message = "El id del producto es obligatorio")
        @Schema(example = "1")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "Debe vender al menos 1 unidad")
        @Schema(example = "3")
        Integer cantidad
) {
}
