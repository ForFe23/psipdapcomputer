package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MantenimientoJpa;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MantenimientoMapper {
    Mantenimiento toDomain(MantenimientoJpa origen);
    MantenimientoJpa toJpa(Mantenimiento origen);
}

