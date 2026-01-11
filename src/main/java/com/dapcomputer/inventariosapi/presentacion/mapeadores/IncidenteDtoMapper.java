package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.presentacion.dto.IncidenteDto;

public class IncidenteDtoMapper {
    public Incidente toDomain(IncidenteDto origen) {
        return new Incidente(
                origen.id(),
                origen.serieEquipo(),
                origen.idUsuario(),
                origen.idCliente(),
                origen.fechaIncidente(),
                origen.detalle(),
                origen.costo(),
                origen.tecnico(),
                origen.cliente(),
                origen.responsable());
    }

    public IncidenteDto toDto(Incidente origen) {
        return new IncidenteDto(
                origen.id(),
                origen.serieEquipo(),
                origen.idUsuario(),
                origen.idCliente(),
                origen.fechaIncidente(),
                origen.detalle(),
                origen.costo(),
                origen.tecnico(),
                origen.cliente(),
                origen.responsable());
    }

    public java.util.List<IncidenteDto> toDtoList(java.util.List<Incidente> origen) {
        return origen.stream().map(this::toDto).toList();
    }
}
