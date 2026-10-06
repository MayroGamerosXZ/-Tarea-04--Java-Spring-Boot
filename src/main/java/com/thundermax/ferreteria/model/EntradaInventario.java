package com.thundermax.ferreteria.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Mercadería que llega de un proveedor. Aumenta el stock del producto. */
@Entity
@Table(name = "entradas_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(nullable = false)
    private Integer cantidad;

    /** Lo que le costó a la ferretería cada unidad. */
    @Column(name = "costo_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "costo_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "numero_factura", length = 40)
    private String numeroFactura;

    @Column(length = 255)
    private String observaciones;
}
