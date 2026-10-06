package com.thundermax.ferreteria.model.enums;

/** Tipos de movimiento que se registran en el kárdex de cada producto. */
public enum TipoMovimiento {
    /** Stock con el que se creó el producto. */
    INVENTARIO_INICIAL,
    /** Mercadería recibida de un proveedor (suma stock). */
    ENTRADA,
    /** Salida por venta (resta stock). */
    SALIDA_VENTA,
    /** Devolución por anulación de venta (suma stock). */
    DEVOLUCION_ANULACION
}
