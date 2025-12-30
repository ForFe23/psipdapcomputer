package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.IncidenteJpa;

public class IncidenteMapper {
    public Incidente toDomain(IncidenteJpa origen) {
        if (origen == null) {
            return null;
        }
        return new Incidente(origen.getId(), origen.getSerieEquipo(), origen.getIdUsuario(), origen.getIdCliente(), origen.getFechaIncidente(), origen.getDetalle(), origen.getCosto(), origen.getTecnico(), origen.getCliente(), origen.getResponsable());
    }

    public IncidenteJpa toJpa(Incidente origen) {
        if (origen == null) {
            return null;
        }
        return IncidenteJpa.builder()
                .id(origen.id())
                .serieEquipo(origen.serieEquipo())
                .idUsuario(origen.idUsuario())
                .idCliente(origen.idCliente())
                .fechaIncidente(origen.fechaIncidente())
                .detalle(origen.detalle())
                .costo(origen.costo())
                .tecnico(origen.tecnico())
                .cliente(origen.cliente())
                .responsable(origen.responsable())
                .build();
    }
}
