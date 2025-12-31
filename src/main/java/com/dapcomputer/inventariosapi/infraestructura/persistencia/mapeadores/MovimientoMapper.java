package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;

public class MovimientoMapper {
    public Movimiento toDomain(MovimientoJpa origen) {
        if (origen == null) {
            return null;
        }
        return new Movimiento(origen.getId(), origen.getActa() != null ? origen.getActa().getId() : null, origen.getIdEquipo(), origen.getSerieEquipo(), origen.getTipo(), origen.getUbicacionOrigen(), origen.getUbicacionDestino(), origen.getIdUsuarioOrigen(), origen.getIdUsuarioDestino(), origen.getFecha(), origen.getObservacion());
    }

    public MovimientoJpa toJpa(Movimiento origen, ActaJpa acta) {
        if (origen == null) {
            return null;
        }
        return MovimientoJpa.builder()
                .id(origen.id())
                .acta(acta)
                .idEquipo(origen.idEquipo())
                .serieEquipo(origen.serieEquipo())
                .tipo(origen.tipo())
                .ubicacionOrigen(origen.ubicacionOrigen())
                .ubicacionDestino(origen.ubicacionDestino())
                .idUsuarioOrigen(origen.idUsuarioOrigen())
                .idUsuarioDestino(origen.idUsuarioDestino())
                .fecha(origen.fecha())
                .observacion(origen.observacion())
                .build();
    }
}
