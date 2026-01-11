package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpaId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipoMapper {
    @Mapping(target = "identificador", expression = "java(new EquipoId(origen.getId(), origen.getSerieEquipo()))")
    Equipo toDomain(EquipoJpa origen);

    @Mapping(target = "id", expression = "java(origen.identificador() != null ? origen.identificador().id() : null)")
    @Mapping(target = "serieEquipo", expression = "java(origen.identificador() != null ? origen.identificador().serie() : null)")
    EquipoJpa toJpa(Equipo origen);
}
