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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perifericos", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerifericoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EQUIPO_ID", nullable = false)
    private Integer equipoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPO_ID", insertable = false, updatable = false)
    private EquipoJpa equipo;

    @Column(name = "SERIEEQUIPO", length = 50)
    private String serieEquipo;

    @Column(name = "SERIEMONITOR", length = 50)
    private String serieMonitor;

    @Column(name = "ACTIVOMONITOR", length = 50)
    private String activoMonitor;

    @Column(name = "MARCAMONITOR", length = 50)
    private String marcaMonitor;

    @Column(name = "MODELOMONITOR", length = 50)
    private String modeloMonitor;

    @Column(name = "OBSERVACIONMONITOR", length = 100)
    private String observacionMonitor;

    @Column(name = "SERIETECLADO", length = 50)
    private String serieTeclado;

    @Column(name = "ACTIVOTECLADO", length = 50)
    private String activoTeclado;

    @Column(name = "MARCATECLADO", length = 50)
    private String marcaTeclado;

    @Column(name = "MODELOTECLADO", length = 50)
    private String modeloTeclado;

    @Column(name = "OBSERVACIONTECLADO", length = 100)
    private String observacionTeclado;

    @Column(name = "SERIEMOUSE", length = 50)
    private String serieMouse;

    @Column(name = "ACTIVOMOUSE", length = 50)
    private String activoMouse;

    @Column(name = "MARCAMOUSE", length = 50)
    private String marcaMouse;

    @Column(name = "MODELOMOUSE", length = 50)
    private String modeloMouse;

    @Column(name = "OBSERVACIONMOUSE", length = 100)
    private String observacionMouse;

    @Column(name = "SERIETELEFONO", length = 50)
    private String serieTelefono;

    @Column(name = "ACTIVOTELEFONO", length = 50)
    private String activoTelefono;

    @Column(name = "MARCATELEFONO", length = 50)
    private String marcaTelefono;

    @Column(name = "MODELOTELEFONO", length = 50)
    private String modeloTelefono;

    @Column(name = "OBSERVACIONTELEFONO", length = 100)
    private String observacionTelefono;

    @Column(name = "CLIENTEPERIFERICOS", length = 500)
    private String clientePerifericos;

    @Column(name = "IDCLIENTE")
    private Integer idCliente;

    @Column(name = "ESTADO_INTERNO", length = 50)
    private String estadoInterno;
}

