package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.presentacion.dto.EquipoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipoDtoMapper {
    Equipo toDomain(EquipoDto origen);
    EquipoDto toDto(Equipo origen);
}

