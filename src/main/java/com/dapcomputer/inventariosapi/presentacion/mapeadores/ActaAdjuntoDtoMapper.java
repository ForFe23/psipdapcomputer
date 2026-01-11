package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaAdjuntoDto;
import java.util.List;

public class ActaAdjuntoDtoMapper {
    public ActaAdjunto toDomain(ActaAdjuntoDto origen) {
        if (origen == null) {
            return null;
        }
        return new ActaAdjunto(origen.id(), origen.idActa(), origen.nombre(), origen.url(), origen.tipo());
    }

    public ActaAdjuntoDto toDto(ActaAdjunto origen) {
        if (origen == null) {
            return null;
        }
        return new ActaAdjuntoDto(origen.id(), origen.idActa(), origen.nombre(), origen.url(), origen.tipo());
    }

    public List<ActaAdjuntoDto> toDtoList(List<ActaAdjunto> origen) {
        return origen.stream().map(this::toDto).toList();
    }
}
