package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acta_seq", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActaSecuenciaJpa {
    @EmbeddedId
    private ActaSecuenciaJpaId id;

    @Column(name = "seq")
    private Integer secuencia;
}

