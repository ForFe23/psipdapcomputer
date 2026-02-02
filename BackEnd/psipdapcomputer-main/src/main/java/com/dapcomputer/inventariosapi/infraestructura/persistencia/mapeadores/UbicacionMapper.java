package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UbicacionJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {
    Ubicacion toDomain(UbicacionJpa origen);
    UbicacionJpa toJpa(Ubicacion origen);
}
