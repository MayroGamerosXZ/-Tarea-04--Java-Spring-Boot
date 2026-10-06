package com.thundermax.ferreteria.dto;

import com.thundermax.ferreteria.model.Producto;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        String marca,
        BigDecimal precio,
        Integer stock,
        Integer stockMinimo,
        String unidad,
        boolean stockBajo,
        String categoria,
        String proveedor
) {
    /** Convierte una Entidad en un DTO para enviarlo como JSON sin ciclos ni datos innecesarios. */
    public static ProductoResponse de(Producto p) {
        return new ProductoResponse(
                p.getId(), p.getCodigo(), p.getNombre(), p.getDescripcion(), p.getMarca(),
                p.getPrecio(), p.getStock(), p.getStockMinimo(), p.getUnidad().name(),
                p.isStockBajo(),
                p.getCategoria().getNombre(),
                p.getProveedor() != null ? p.getProveedor().getNombre() : null
        );
    }
}
