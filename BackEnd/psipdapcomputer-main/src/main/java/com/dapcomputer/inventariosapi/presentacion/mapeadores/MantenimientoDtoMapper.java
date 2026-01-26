package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.presentacion.dto.MantenimientoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MantenimientoDtoMapper {
    Mantenimiento toDomain(MantenimientoDto origen);
    MantenimientoDto toDto(Mantenimiento origen);
}
