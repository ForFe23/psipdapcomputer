package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCLIENTE")
    private Long id;

    @Column(name = "NOMBRECLIENTE", length = 50)
    private String nombre;

    @Column(name = "CONTRASENACLIENTE", length = 50)
    private String contrasena;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "LICENCIACLIENTE", length = 50)
    private String licencia;

    @Column(name = "Calle", length = 50)
    private String calle;

    @Column(name = "Ciudad", length = 50)
    private String ciudad;

    @Column(name = "FECHALICENCIA")
    private LocalDate fechaLicencia;

    @Column(name = "ESTADO_INTERNO", length = 50)
    private String estadoInterno;
}
