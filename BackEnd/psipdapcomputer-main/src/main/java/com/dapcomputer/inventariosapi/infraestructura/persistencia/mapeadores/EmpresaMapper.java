package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Empresa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EmpresaJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    Empresa toDomain(EmpresaJpa origen);
    EmpresaJpa toJpa(Empresa origen);
}
