package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerifericoMapper {
    @Mapping(target = "equipoId", source = "equipoId")
    Periferico toDomain(PerifericoJpa origen);

    @Mapping(target = "equipo", ignore = true)
    PerifericoJpa toJpa(Periferico origen);
}

