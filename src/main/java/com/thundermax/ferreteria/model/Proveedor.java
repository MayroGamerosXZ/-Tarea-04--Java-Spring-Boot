package com.thundermax.ferreteria.model;

import jakarta.persistence.*;
import lombok.*;

/** Empresa que surte mercadería a la ferretería. */
@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(unique = true, length = 20)
    private String nit;

    @Column(length = 100)
    private String contacto;

    @Column(length = 20)
    private String telefono;

    @Column(length = 120)
    private String email;

    @Column(length = 255)
    private String direccion;
}
