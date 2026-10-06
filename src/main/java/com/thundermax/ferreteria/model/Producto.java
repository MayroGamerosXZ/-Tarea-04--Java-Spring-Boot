package com.thundermax.ferreteria.model;

import com.thundermax.ferreteria.model.enums.UnidadMedida;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Producto que se vende en la ferretería.
 * <p>
 * Reglas importantes:
 * <ul>
 *   <li>El {@code precio} se guarda SIN IVA; el IVA (12 %) se calcula al vender.</li>
 *   <li>El {@code stock} NO se edita directamente: solo cambia con entradas, ventas y anulaciones
 *       (así el kárdex siempre cuadra).</li>
 *   <li>Eliminar un producto es un "borrado lógico": se marca {@code activo = false}
 *       para no perder su historial de ventas.</li>
 * </ul>
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código interno, p. ej. HER-001. */
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 60)
    private String marca;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Enumerated(EnumType.STRING) // guarda el texto "GALON" en lugar de un número
    @Column(nullable = false, length = 20)
    private UnidadMedida unidad;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    /** Muchos productos pertenecen a UNA categoría (llave foránea categoria_id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /** Proveedor habitual del producto (opcional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    public boolean isStockBajo() {
        return stock != null && stockMinimo != null && stock <= stockMinimo;
    }
}
