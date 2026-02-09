package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ClienteJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EmpresaJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "cliente_id", nullable = true)
    private Long clienteId;

    @ManyToOne
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private ClienteJpa cliente;

    @Column(name = "empresa_id", nullable = true)
    private Long empresaId;

    @ManyToOne
    @JoinColumn(name = "empresa_id", insertable = false, updatable = false)
    private EmpresaJpa empresa;

    @Column(nullable = false, length = 50)
    private String cedula;

    @Column(nullable = false, length = 120)
    private String apellidos;

    @Column(nullable = false, length = 120)
    private String nombres;

    @Column(length = 120)
    private String correo;

    @Column(length = 50)
    private String telefono;

    @Column(nullable = false, length = 40)
    private String cargo;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;
}

