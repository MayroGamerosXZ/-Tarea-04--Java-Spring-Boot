package com.thundermax.ferreteria.repository;

import com.thundermax.ferreteria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Al extender JpaRepository obtenemos GRATIS: findAll, findById, save, deleteById, count...
 * Spring genera la implementación en tiempo de ejecución (¡no escribimos SQL!).
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Spring "lee" el nombre del método y genera: SELECT ... WHERE LOWER(nombre) = LOWER(?)
    boolean existsByNombreIgnoreCase(String nombre);
}
