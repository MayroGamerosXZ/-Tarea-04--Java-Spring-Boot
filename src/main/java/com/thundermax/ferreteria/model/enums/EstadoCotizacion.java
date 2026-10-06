package com.thundermax.ferreteria.model.enums;

/**
 * Estado de una cotización (proforma).
 * VIGENTE: se puede convertir en venta. CONVERTIDA: ya generó una venta.
 * VENCIDA: pasó su fecha de vencimiento.
 */
public enum EstadoCotizacion {
    VIGENTE,
    CONVERTIDA,
    VENCIDA
}
