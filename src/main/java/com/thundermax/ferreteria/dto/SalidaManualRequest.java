package com.thundermax.ferreteria.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SalidaManualRequest(
        @NotNull Long productoId,
        @Min(1) int cantidad,
        String motivo
) {
}
