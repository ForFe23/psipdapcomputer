package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Persona;
import com.dapcomputer.inventariosapi.presentacion.dto.PersonaDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonaDtoMapper {
    PersonaDto toDto(Persona origen);
    Persona toDomain(PersonaDto origen);
}

