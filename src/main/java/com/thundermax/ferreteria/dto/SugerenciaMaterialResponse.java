package com.thundermax.ferreteria.dto;

import java.util.List;

public record SugerenciaMaterialResponse(
        String material,
        Double areaMetrosCuadrados,
        Integer cantidadNecesaria,
        String unidadRequerida,
        List<ProductoResponse> productosSugeridosEnStock
) {
}
