package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {
    @Mapping(target = "acta", source = "acta")
    @Mapping(target = "id", source = "origen.id")
    @Mapping(target = "empresaId", source = "origen.empresaId")
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "idEquipo", source = "origen.idEquipo")
    @Mapping(target = "serieEquipo", source = "origen.serieEquipo")
    @Mapping(target = "tipo", source = "origen.tipo")
    @Mapping(target = "estadoInterno", source = "origen.estadoInterno")
    @Mapping(target = "ubicacionOrigen", source = "origen.ubicacionOrigen")
    @Mapping(target = "ubicacionDestino", source = "origen.ubicacionDestino")
    @Mapping(target = "ubicacionOrigenId", source = "origen.ubicacionOrigenId")
    @Mapping(target = "ubicacionDestinoId", source = "origen.ubicacionDestinoId")
    @Mapping(target = "usuarioOrigen", ignore = true)
    @Mapping(target = "usuarioDestino", ignore = true)
    @Mapping(target = "idUsuarioOrigen", source = "origen.idUsuarioOrigen")
    @Mapping(target = "idUsuarioDestino", source = "origen.idUsuarioDestino")
    @Mapping(target = "personaOrigen", ignore = true)
    @Mapping(target = "personaDestino", ignore = true)
    @Mapping(target = "personaOrigenId", source = "origen.personaOrigenId")
    @Mapping(target = "personaDestinoId", source = "origen.personaDestinoId")
    @Mapping(target = "ejecutadoPor", ignore = true)
    @Mapping(target = "ejecutadoPorId", source = "origen.ejecutadoPorId")
    @Mapping(target = "fecha", source = "origen.fecha")
    @Mapping(target = "observacion", source = "origen.observacion")
    MovimientoJpa toJpa(Movimiento origen, ActaJpa acta);

    @Mapping(target = "idActa", expression = "java(origen.getActa() != null ? origen.getActa().getId() : null)")
    Movimiento toDomain(MovimientoJpa origen);
}
