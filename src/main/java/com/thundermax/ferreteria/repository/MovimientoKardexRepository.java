package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.MovimientoKardex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoKardexRepository extends JpaRepository<MovimientoKardex, Long> {

    List<MovimientoKardex> findByProductoIdOrderByFechaAscIdAsc(Long productoId);
}
