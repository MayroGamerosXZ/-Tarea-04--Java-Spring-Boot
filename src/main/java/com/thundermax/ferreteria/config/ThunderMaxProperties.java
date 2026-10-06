package com.thundermax.ferreteria.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Lee las propiedades {@code thundermax.*} de application.properties.
 * Así el IVA no queda "quemado" en el código: si cambia, solo se edita la configuración.
 */
@ConfigurationProperties(prefix = "thundermax")
public record ThunderMaxProperties(BigDecimal iva, String moneda, String simboloMoneda) {

    /** Calcula el IVA de un monto, redondeado a 2 decimales (centavos). */
    public BigDecimal calcularIva(BigDecimal monto) {
        return monto.multiply(iva).setScale(2, RoundingMode.HALF_UP);
    }
}
