package com.thundermax.ferreteria.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Categoría de productos (Herramientas, Pintura, Electricidad...).
 * <p>
 * {@code @Entity} le indica a JPA/Hibernate que esta clase es una TABLA en PostgreSQL.
 * Cada atributo es una COLUMNA. Equivale a definir un modelo con Sequelize en Node.js.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremental (SERIAL en PostgreSQL)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
