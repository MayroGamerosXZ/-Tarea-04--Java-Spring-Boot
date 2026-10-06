package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    boolean existsByNit(String nit);
}
