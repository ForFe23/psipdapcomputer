package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaSecuencia;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpaId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActaSecuenciaMapper {
    @Mapping(target = "idCliente", source = "id.idCliente")
    @Mapping(target = "anio", source = "id.anio")
    ActaSecuencia toDomain(ActaSecuenciaJpa origen);

    @Mapping(target = "id", expression = "java(new ActaSecuenciaJpaId(origen.idCliente(), origen.anio()))")
    ActaSecuenciaJpa toJpa(ActaSecuencia origen);
}

