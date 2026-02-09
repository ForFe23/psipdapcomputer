package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mantenimiento", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MantenimientoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EQUIPO_ID", nullable = false)
    private Long equipoId;

    @Column(name = "SERIEEQUIPO", length = 50)
    private String serieSnapshot;

    @Column(name = "IDCLIENTE")
    private Long idCliente;

    @Column(name = "FECHAPROGRAMADA")
    private LocalDateTime fechaProgramada;

    @Column(name = "FRECUENCIADIAS")
    private Integer frecuenciaDias;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "ESTADO", length = 30)
    private String estado;

    @Column(name = "CREADOEN")
    private LocalDateTime creadoEn;

    @Column(name = "ESTADO_INTERNO", length = 50)
    private String estadoInterno;
}

