package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "equipo_id", referencedColumnName = "ID"),
            @JoinColumn(name = "equipo_serie", referencedColumnName = "SERIEEQUIPO")
    })
    private EquipoJpa equipo;

    @Column(name = "item_num")
    private Integer itemNumero;

    @Column(name = "tipo", length = 120)
    private String tipo;

    @Column(name = "serie", length = 120)
    private String serie;

    @Column(name = "modelo", length = 120)
    private String modelo;

    @Column(name = "observacion", length = 255)
    private String observacion;
}
