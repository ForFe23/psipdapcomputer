package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo", length = 50, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 120)
    private String nombre;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;

    public RolJpa(Long id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.estadoInterno = "ACTIVO_INTERNAL";
    }
}

