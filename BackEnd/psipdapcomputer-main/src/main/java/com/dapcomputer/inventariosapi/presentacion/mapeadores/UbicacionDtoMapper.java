package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import com.dapcomputer.inventariosapi.presentacion.dto.UbicacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UbicacionDtoMapper {
    UbicacionDto toDto(Ubicacion origen);
    Ubicacion toDomain(UbicacionDto origen);
}
