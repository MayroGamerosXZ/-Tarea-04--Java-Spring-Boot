package com.thundermax.ferreteria.model;

import com.thundermax.ferreteria.model.enums.EstadoVenta;
import com.thundermax.ferreteria.model.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Encabezado de una venta. Sus líneas están en {@link DetalleVenta}.
 * <p>
 * {@code cascade = ALL} significa que al guardar la venta también se guardan
 * automáticamente todos sus detalles (no hay que guardarlos uno por uno).
 */
@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVenta estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal iva;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    /** Si la venta vino de una cotización, aquí queda su id. */
    @Column(name = "cotizacion_id")
    private Long cotizacionId;

    @Column(name = "motivo_anulacion", length = 255)
    private String motivoAnulacion;

    @Column(name = "fecha_anulacion")
    private LocalDateTime fechaAnulacion;

    @Builder.Default
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleVenta detalle) {
        detalle.setVenta(this);
        detalles.add(detalle);
    }
}
