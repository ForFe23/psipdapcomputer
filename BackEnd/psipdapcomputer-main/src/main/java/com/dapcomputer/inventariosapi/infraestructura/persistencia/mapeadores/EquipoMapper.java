package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipoMapper {
    @Mapping(target = "serie", source = "serieEquipo")
    Equipo toDomain(EquipoJpa origen);

    @Mapping(target = "serieEquipo", source = "serie")
    EquipoJpa toJpa(Equipo origen);
}
