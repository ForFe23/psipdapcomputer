package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaItem;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActaItemDtoMapper {
    @Mapping(target = "actaId", ignore = true)
    ActaItem toDomain(ActaItemDto origen);
    ActaItemDto toDto(ActaItem origen);
}

