package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoJpaId implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SERIEEQUIPO", length = 50)
    private String serieEquipo;
}
