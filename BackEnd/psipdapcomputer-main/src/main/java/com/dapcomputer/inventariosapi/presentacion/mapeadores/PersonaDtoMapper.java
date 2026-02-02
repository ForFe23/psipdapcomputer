package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Persona;
import com.dapcomputer.inventariosapi.presentacion.dto.PersonaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaDtoMapper {
    PersonaDto toDto(Persona origen);
    Persona toDomain(PersonaDto origen);
}
