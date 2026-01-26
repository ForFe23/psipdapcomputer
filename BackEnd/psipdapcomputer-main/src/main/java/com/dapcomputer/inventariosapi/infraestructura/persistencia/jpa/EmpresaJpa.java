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
@Table(name = "empresa", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;
}
