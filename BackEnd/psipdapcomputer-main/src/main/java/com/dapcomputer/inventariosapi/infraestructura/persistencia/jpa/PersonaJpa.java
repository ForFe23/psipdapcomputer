package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import com.dapcomputer.inventariosapi.dominio.entidades.CargoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persona", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(nullable = false, unique = true, length = 50)
    private String cedula;

    @Column(nullable = false, length = 120)
    private String apellidos;

    @Column(nullable = false, length = 120)
    private String nombres;

    @Column(length = 120)
    private String correo;

    @Column(length = 50)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private CargoUsuario cargo;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;
}
