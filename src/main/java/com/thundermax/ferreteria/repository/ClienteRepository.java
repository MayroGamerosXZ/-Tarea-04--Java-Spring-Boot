package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByNitIgnoreCase(String nit);

    boolean existsByNitIgnoreCase(String nit);
}
