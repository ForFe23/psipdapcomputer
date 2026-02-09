package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Rol;
import com.dapcomputer.inventariosapi.presentacion.dto.RolDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolDtoMapper {
    RolDto toDto(Rol origen);
    Rol toDomain(RolDto origen);
}

