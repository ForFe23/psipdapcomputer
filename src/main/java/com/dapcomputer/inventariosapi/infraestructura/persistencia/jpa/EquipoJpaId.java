package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoJpaId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String serieEquipo;
}
