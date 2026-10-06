package com.thundermax.ferreteria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CotizacionRequest(
        @NotNull(message = "El id del cliente es obligatorio")
        @Schema(example = "1")
        Long clienteId,

        @NotNull(message = "Los días de vigencia son obligatorios")
        @Schema(example = "15")
        Integer diasVigencia,

        @Size(max = 255)
        @Schema(example = "Precios sujetos a disponibilidad de inventario")
        String observaciones,

        @NotEmpty(message = "La cotización debe tener al menos un producto")
        @Valid
        List<DetalleCotizacionRequest> detalles
) {
}
