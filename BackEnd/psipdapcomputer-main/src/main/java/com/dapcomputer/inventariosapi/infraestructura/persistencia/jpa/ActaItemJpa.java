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
@Table(name = "acta_item", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActaItemJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acta_id")
    private ActaJpa acta;

    @Column(name = "equipo_id")
    private Integer equipoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", insertable = false, updatable = false)
    private EquipoJpa equipo;

    @Column(name = "equipo_serie", length = 120)
    private String equipoSerie;

    @Column(name = "item_num")
    private Integer itemNumero;

    @Column(name = "tipo", length = 120)
    private String tipo;

    @Column(name = "serie", length = 120)
    private String serie;

    @Column(name = "modelo", length = 120)
    private String modelo;

    @Column(name = "marca", length = 120)
    private String marca;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;
}

