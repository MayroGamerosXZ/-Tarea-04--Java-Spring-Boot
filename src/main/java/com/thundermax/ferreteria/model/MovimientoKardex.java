package com.thundermax.ferreteria.model;

import com.thundermax.ferreteria.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Kárdex: bitácora de TODOS los movimientos de stock de un producto.
 * Cada registro guarda el stock antes y después, así se puede auditar
 * por qué un producto tiene la existencia que tiene.
 */
@Entity
@Table(name = "movimientos_kardex")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoKardex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimiento tipo;

    /** Positivo si entra, negativo si sale. */
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_nuevo", nullable = false)
    private Integer stockNuevo;

    /** Ej.: "Venta #12", "Entrada #3 - Factura A-555". */
    @Column(length = 120)
    private String referencia;

    @Column(name = "costo_unitario", precision = 12, scale = 2)
    private java.math.BigDecimal costoUnitario;

    @Column(name = "precio_venta", precision = 12, scale = 2)
    private java.math.BigDecimal precioVenta;
}
