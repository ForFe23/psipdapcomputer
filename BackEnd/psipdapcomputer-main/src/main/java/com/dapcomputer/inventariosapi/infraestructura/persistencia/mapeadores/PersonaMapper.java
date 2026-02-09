package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Persona;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PersonaJpa;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonaMapper {
    Persona toDomain(PersonaJpa origen);
    PersonaJpa toJpa(Persona origen);
}

