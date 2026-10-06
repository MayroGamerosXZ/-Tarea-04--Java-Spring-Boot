package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.EntradaInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntradaInventarioRepository extends JpaRepository<EntradaInventario, Long> {

    List<EntradaInventario> findAllByOrderByFechaDesc();
}
