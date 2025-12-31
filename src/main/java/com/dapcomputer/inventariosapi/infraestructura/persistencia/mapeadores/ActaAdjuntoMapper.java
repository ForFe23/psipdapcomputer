package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaAdjuntoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;

public class ActaAdjuntoMapper {
    public ActaAdjunto toDomain(ActaAdjuntoJpa origen) {
        if (origen == null) {
            return null;
        }
        return new ActaAdjunto(origen.getId(), origen.getActa() != null ? origen.getActa().getId() : null, origen.getNombre(), origen.getUrl(), origen.getTipo());
    }

    public ActaAdjuntoJpa toJpa(ActaAdjunto origen, ActaJpa acta) {
        if (origen == null) {
            return null;
        }
        return ActaAdjuntoJpa.builder()
                .id(origen.id())
                .acta(acta)
                .nombre(origen.nombre())
                .url(origen.url())
                .tipo(origen.tipo())
                .build();
    }
}
