package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.Producto;
import com.thundermax.ferreteria.model.enums.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrueOrderByNombreAsc();

    List<Producto> findByActivoTrueAndCategoriaIdOrderByNombreAsc(Long categoriaId);

    List<Producto> findByActivoTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);

    boolean existsByCodigoIgnoreCase(String codigo);

    /** Productos cuyo stock llegó o bajó del mínimo. Consulta escrita en JPQL (sobre clases, no tablas). */
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stock <= p.stockMinimo ORDER BY p.stock ASC")
    List<Producto> findStockBajo();

    /** Usada por la calculadora de materiales para sugerir productos. */
    List<Producto> findByActivoTrueAndUnidadAndNombreContainingIgnoreCaseOrderByPrecioAsc(
            UnidadMedida unidad, String nombre);

    @Query("SELECT COALESCE(SUM(p.precio * p.stock), 0) FROM Producto p WHERE p.activo = true")
    BigDecimal calcularValorInventario();
}
