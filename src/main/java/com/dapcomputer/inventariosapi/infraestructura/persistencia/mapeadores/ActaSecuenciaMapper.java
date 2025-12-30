package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaSecuencia;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpaId;

public class ActaSecuenciaMapper {
    public ActaSecuencia toDomain(ActaSecuenciaJpa origen) {
        if (origen == null || origen.getId() == null) {
            return null;
        }
        return new ActaSecuencia(origen.getId().getIdCliente(), origen.getId().getAnio(), origen.getSecuencia());
    }

    public ActaSecuenciaJpa toJpa(ActaSecuencia origen) {
        if (origen == null) {
            return null;
        }
        ActaSecuenciaJpaId id = new ActaSecuenciaJpaId(origen.idCliente(), origen.anio());
        return ActaSecuenciaJpa.builder().id(id).secuencia(origen.secuencia()).build();
    }
}
