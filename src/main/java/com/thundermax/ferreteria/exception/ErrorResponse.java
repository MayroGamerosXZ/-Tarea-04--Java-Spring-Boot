package com.thundermax.ferreteria.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/** Formato único de error que devuelve TODA la API. */
@Schema(description = "Respuesta estándar de error")
public record ErrorResponse(
        @Schema(example = "2026-10-06T10:15:30") LocalDateTime timestamp,
        @Schema(example = "409") int status,
        @Schema(example = "Conflict") String error,
        @Schema(example = "Stock insuficiente para 'Martillo de uña 16 oz': disponible 3, solicitado 5") String mensaje,
        @Schema(example = "/api/ventas") String ruta,
        @Schema(description = "Detalle por campo cuando falla la validación") List<String> detalles
) {
}
