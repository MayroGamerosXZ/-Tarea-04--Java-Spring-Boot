package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    List<Cotizacion> findAllByOrderByFechaDesc();
}
