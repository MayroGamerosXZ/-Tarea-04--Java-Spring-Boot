package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.Venta;
import com.thundermax.ferreteria.model.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findAllByOrderByFechaDesc();

    List<Venta> findByFechaBetweenOrderByFechaDesc(LocalDateTime desde, LocalDateTime hasta);

    List<Venta> findByClienteIdOrderByFechaDesc(Long clienteId);

    /**
     * Top de productos más vendidos (solo ventas COMPLETADAS).
     * Devuelve filas [productoId, codigo, nombre, cantidadVendida, totalVendido].
     */
    @Query("""
            SELECT d.producto.id, d.producto.codigo, d.producto.nombre, SUM(d.cantidad), SUM(d.subtotal)
            FROM DetalleVenta d
            WHERE d.venta.estado = :estado
            GROUP BY d.producto.id, d.producto.codigo, d.producto.nombre
            ORDER BY SUM(d.cantidad) DESC
            """)
    List<Object[]> topProductos(@Param("estado") EstadoVenta estado);
}
