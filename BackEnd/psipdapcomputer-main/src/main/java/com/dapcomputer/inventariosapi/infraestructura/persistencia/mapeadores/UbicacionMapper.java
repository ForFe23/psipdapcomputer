package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UbicacionJpa;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UbicacionMapper {
    Ubicacion toDomain(UbicacionJpa origen);
    UbicacionJpa toJpa(Ubicacion origen);
}

