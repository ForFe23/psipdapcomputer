package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.presentacion.dto.IncidenteDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncidenteDtoMapper {
    Incidente toDomain(IncidenteDto origen);

    IncidenteDto toDto(Incidente origen);
}
