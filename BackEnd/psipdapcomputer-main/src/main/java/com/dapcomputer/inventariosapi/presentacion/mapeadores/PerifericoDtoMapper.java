package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.presentacion.dto.PerifericoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerifericoDtoMapper {
    Periferico toDomain(PerifericoDto origen);

    PerifericoDto toDto(Periferico origen);
}

