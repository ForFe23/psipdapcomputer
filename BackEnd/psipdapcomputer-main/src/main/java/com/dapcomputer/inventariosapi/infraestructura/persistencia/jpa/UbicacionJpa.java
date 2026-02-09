package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ClienteJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EmpresaJpa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ubicacion", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;
}

