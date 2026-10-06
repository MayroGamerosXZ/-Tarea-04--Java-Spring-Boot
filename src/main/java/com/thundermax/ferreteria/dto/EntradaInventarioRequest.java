package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EntradaInventarioRequest(
        @NotNull(message = "El id del producto es obligatorio")
        @Schema(example = "1")
        Long productoId,

        @NotNull(message = "El id del proveedor es obligatorio")
        @Schema(example = "1")
        Long proveedorId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "Debe ingresar al menos 1 unidad")
        @Schema(example = "50")
        Integer cantidad,

        @NotNull(message = "El costo unitario es obligatorio")
        @Min(value = 0, message = "El costo no puede ser negativo")
        @Schema(example = "45.00")
        BigDecimal costoUnitario,

        @Size(max = 40)
        @Schema(example = "FACT-998877")
        String numeroFactura,

        @Size(max = 255)
        @Schema(example = "Ingreso a bodega principal")
        String observaciones
) {
}
