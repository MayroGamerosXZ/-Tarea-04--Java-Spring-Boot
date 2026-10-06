package com.thundermax.ferreteria.dto;

import com.thundermax.ferreteria.model.enums.MetodoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VentaRequest(
        @NotNull(message = "El id del cliente es obligatorio")
        @Schema(example = "1")
        Long clienteId,

        @NotNull(message = "El método de pago es obligatorio")
        @Schema(example = "EFECTIVO")
        MetodoPago metodoPago,

        @NotEmpty(message = "La venta debe tener al menos un producto")
        @Valid // Obliga a validar cada DetalleVentaRequest por dentro
        List<DetalleVentaRequest> detalles
) {
}
