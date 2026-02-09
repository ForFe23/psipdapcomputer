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
public class ActaSecuenciaJpaId implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "idCliente")
    private Integer idCliente;

    @Column(name = "anio")
    private Integer anio;
}

