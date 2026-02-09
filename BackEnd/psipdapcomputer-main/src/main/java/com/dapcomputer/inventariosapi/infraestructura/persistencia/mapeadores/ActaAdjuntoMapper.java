package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaAdjuntoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActaAdjuntoMapper {
    @Mapping(target = "acta", expression = "java(acta)")
    @Mapping(target = "id", source = "origen.id")
    @Mapping(target = "nombre", source = "origen.nombre")
    @Mapping(target = "url", source = "origen.url")
    @Mapping(target = "tipo", source = "origen.tipo")
    @Mapping(target = "estadoInterno", source = "origen.estadoInterno")
    ActaAdjuntoJpa toJpa(ActaAdjunto origen, ActaJpa acta);

    @Mapping(target = "idActa", expression = "java(origen.getActa() != null ? origen.getActa().getId() : null)")
    @Mapping(target = "id", source = "origen.id")
    @Mapping(target = "nombre", source = "origen.nombre")
    @Mapping(target = "url", source = "origen.url")
    @Mapping(target = "tipo", source = "origen.tipo")
    @Mapping(target = "estadoInterno", source = "origen.estadoInterno")
    ActaAdjunto toDomain(ActaAdjuntoJpa origen);
}

