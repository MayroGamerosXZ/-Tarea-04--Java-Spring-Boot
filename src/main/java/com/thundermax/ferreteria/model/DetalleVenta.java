package com.thundermax.ferreteria.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Línea de una venta: qué producto, cuántos y a qué precio.
 * Se guarda el {@code precioUnitario} del momento de la venta, para que si el precio
 * del producto cambia mañana, la venta histórica no se altere.
 */
@Entity
@Table(name = "detalle_ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
}
