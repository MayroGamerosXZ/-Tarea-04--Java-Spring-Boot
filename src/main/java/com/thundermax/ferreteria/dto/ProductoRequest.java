package com.thundermax.ferreteria.dto;

import com.thundermax.ferreteria.model.enums.UnidadMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 30)
        @Schema(example = "HER-009")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150)
        @Schema(example = "Alicate de presión 10\"")
        String nombre,

        @Size(max = 255)
        @Schema(example = "Alicate con mordaza curva")
        String descripcion,

        @Size(max = 60)
        @Schema(example = "Irwin")
        String marca,

        @NotNull(message = "El precio es obligatorio")
        @Min(value = 0, message = "El precio no puede ser negativo")
        @Schema(example = "85.50")
        BigDecimal precio,

        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo")
        @Schema(example = "5")
        Integer stockMinimo,

        @NotNull(message = "La unidad de medida es obligatoria")
        @Schema(example = "UNIDAD")
        UnidadMedida unidad,

        @NotNull(message = "El id de la categoría es obligatorio")
        @Schema(example = "1")
        Long categoriaId,

        @Schema(example = "1")
        Long proveedorId
) {
}
