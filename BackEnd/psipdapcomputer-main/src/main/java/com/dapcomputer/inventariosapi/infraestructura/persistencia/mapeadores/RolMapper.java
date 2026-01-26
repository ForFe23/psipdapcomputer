package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Rol;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.RolJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {
    Rol toDomain(RolJpa origen);
    RolJpa toJpa(Rol origen);
}
