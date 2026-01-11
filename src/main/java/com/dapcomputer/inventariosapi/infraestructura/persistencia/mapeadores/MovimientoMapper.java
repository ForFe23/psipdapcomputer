package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {
    @Mapping(target = "acta", expression = "java(acta)")
    @Mapping(target = "id", source = "origen.id")
    @Mapping(target = "idEquipo", source = "origen.idEquipo")
    @Mapping(target = "serieEquipo", source = "origen.serieEquipo")
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "usuarioOrigen", ignore = true)
    @Mapping(target = "usuarioDestino", ignore = true)
    @Mapping(target = "tipo", source = "origen.tipo")
    @Mapping(target = "ubicacionOrigen", source = "origen.ubicacionOrigen")
    @Mapping(target = "ubicacionDestino", source = "origen.ubicacionDestino")
    @Mapping(target = "idUsuarioOrigen", source = "origen.idUsuarioOrigen")
    @Mapping(target = "idUsuarioDestino", source = "origen.idUsuarioDestino")
    @Mapping(target = "fecha", source = "origen.fecha")
    @Mapping(target = "observacion", source = "origen.observacion")
    MovimientoJpa toJpa(Movimiento origen, ActaJpa acta);

    @Mapping(target = "idActa", expression = "java(origen.getActa() != null ? origen.getActa().getId() : null)")
    @Mapping(target = "id", source = "origen.id")
    @Mapping(target = "idEquipo", source = "origen.idEquipo")
    @Mapping(target = "serieEquipo", source = "origen.serieEquipo")
    @Mapping(target = "tipo", source = "origen.tipo")
    @Mapping(target = "ubicacionOrigen", source = "origen.ubicacionOrigen")
    @Mapping(target = "ubicacionDestino", source = "origen.ubicacionDestino")
    @Mapping(target = "idUsuarioOrigen", source = "origen.idUsuarioOrigen")
    @Mapping(target = "idUsuarioDestino", source = "origen.idUsuarioDestino")
    @Mapping(target = "fecha", source = "origen.fecha")
    @Mapping(target = "observacion", source = "origen.observacion")
    Movimiento toDomain(MovimientoJpa origen);
}
