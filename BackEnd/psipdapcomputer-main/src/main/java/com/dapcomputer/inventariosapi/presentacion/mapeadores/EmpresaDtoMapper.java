package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Empresa;
import com.dapcomputer.inventariosapi.presentacion.dto.EmpresaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpresaDtoMapper {
    Empresa toDomain(EmpresaDto origen);
    EmpresaDto toDto(Empresa origen);
}

