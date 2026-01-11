package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.IncidenteJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncidenteMapper {
    Incidente toDomain(IncidenteJpa origen);

    IncidenteJpa toJpa(Incidente origen);
}
