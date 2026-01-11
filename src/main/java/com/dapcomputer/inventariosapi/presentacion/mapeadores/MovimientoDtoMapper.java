package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.presentacion.dto.MovimientoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoDtoMapper {
    Movimiento toDomain(MovimientoDto origen);

    @Mapping(target = "idActa", source = "idActa")
    MovimientoDto toDto(Movimiento origen);
}
