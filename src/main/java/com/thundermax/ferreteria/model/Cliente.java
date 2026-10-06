package com.thundermax.ferreteria.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Cliente de la ferretería. En Guatemala se identifica con su NIT;
 * "CF" (Consumidor Final) se usa cuando el cliente no da NIT.
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    public static final String NIT_CONSUMIDOR_FINAL = "CF";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    @Column(length = 20)
    private String telefono;

    @Column(length = 120)
    private String email;

    @Column(length = 255)
    private String direccion;
}
