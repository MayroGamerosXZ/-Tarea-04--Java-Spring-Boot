package com.thundermax.ferreteria.model;

import com.thundermax.ferreteria.model.enums.EstadoCotizacion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cotización / proforma: el cliente pide precios sin comprar todavía.
 * NO afecta el stock. Puede convertirse en venta mientras esté VIGENTE.
 */
@Entity
@Table(name = "cotizaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCotizacion estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal iva;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(length = 255)
    private String observaciones;

    /** Id de la venta generada al convertirla. */
    @Column(name = "venta_id")
    private Long ventaId;

    @Builder.Default
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleCotizacion detalle) {
        detalle.setCotizacion(this);
        detalles.add(detalle);
    }
}
