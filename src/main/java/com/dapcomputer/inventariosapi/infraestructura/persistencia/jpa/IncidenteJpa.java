package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;

@Entity
@Table(name = "incidente", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidenteJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SERIEEQUIPO", length = 50)
    private String serieEquipo;

    @Column(name = "IDUSUARIO")
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO", insertable = false, updatable = false)
    private UsuarioJpa usuario;

    @Column(name = "IDCLIENTE")
    private Long idCliente;

    @Column(name = "FECHAINCIDENTE")
    private LocalDateTime fechaIncidente;

    @Column(name = "DETALLEINCIDENTE", length = 255)
    private String detalle;

    @Column(name = "COSTOINCIDENTE", length = 50)
    private String costo;

    @Column(name = "tecnicoincidente", length = 50)
    private String tecnico;

    @Column(name = "clienteincidente", length = 50)
    private String cliente;

    @Column(name = "RESPONSABLE", length = 100)
    private String responsable;
}
