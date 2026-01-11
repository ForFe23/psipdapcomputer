package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaAdjuntoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActaAdjuntoDtoMapper {
    ActaAdjunto toDomain(ActaAdjuntoDto origen);

    ActaAdjuntoDto toDto(ActaAdjunto origen);
}
