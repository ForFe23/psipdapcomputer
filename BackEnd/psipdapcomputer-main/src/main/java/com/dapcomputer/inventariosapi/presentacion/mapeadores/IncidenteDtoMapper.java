package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.presentacion.dto.IncidenteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidenteDtoMapper {
    @Mapping(target = "serieEquipo", ignore = true)
    IncidenteDto toDto(Incidente origen);
}

