package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpaId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerifericoMapper {
    @Mapping(target = "id", source = "id.id")
    @Mapping(target = "serieEquipo", source = "id.serieEquipo")
    Periferico toDomain(PerifericoJpa origen);

    @Mapping(target = "id", expression = "java(toJpaId(origen.id(), origen.serieEquipo()))")
    PerifericoJpa toJpa(Periferico origen);

    default EquipoId aIdEquipo(PerifericoJpa origen) {
        if (origen == null || origen.getId() == null) {
            return null;
        }
        return new EquipoId(origen.getId().getId(), origen.getId().getSerieEquipo());
    }

    default PerifericoJpaId toJpaId(Integer id, String serie) {
        if (id == null && serie == null) {
            return null;
        }
        return new PerifericoJpaId(id, serie);
    }
}
