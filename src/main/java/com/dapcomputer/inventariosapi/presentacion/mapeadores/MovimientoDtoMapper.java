package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.presentacion.dto.MovimientoDto;
import java.util.List;

public class MovimientoDtoMapper {
    public Movimiento toDomain(MovimientoDto origen) {
        if (origen == null) {
            return null;
        }
        return new Movimiento(origen.id(), origen.idActa(), origen.idEquipo(), origen.serieEquipo(), origen.tipo(), origen.ubicacionOrigen(), origen.ubicacionDestino(), origen.idUsuarioOrigen(), origen.idUsuarioDestino(), origen.fecha(), origen.observacion());
    }

    public MovimientoDto toDto(Movimiento origen) {
        if (origen == null) {
            return null;
        }
        return new MovimientoDto(origen.id(), origen.idActa(), origen.idEquipo(), origen.serieEquipo(), origen.tipo(), origen.ubicacionOrigen(), origen.ubicacionDestino(), origen.idUsuarioOrigen(), origen.idUsuarioDestino(), origen.fecha(), origen.observacion());
    }

    public List<MovimientoDto> toDtoList(List<Movimiento> origen) {
        return origen.stream().map(this::toDto).toList();
    }
}
