package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.presentacion.dto.MovimientoDto;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDtoMapper {
    public Movimiento toDomain(MovimientoDto origen) {
        if (origen == null) {
            return null;
        }
        return new Movimiento(
                origen.id(),
                origen.idActa(),
                origen.idEquipo(),
                origen.serieEquipo(),
                origen.empresaId(),
                origen.ubicacionOrigenId(),
                origen.ubicacionDestinoId(),
                origen.personaOrigenId(),
                origen.personaDestinoId(),
                origen.ejecutadoPorId(),
                origen.tipo(),
                origen.ubicacionOrigen(),
                origen.ubicacionDestino(),
                origen.idUsuarioOrigen(),
                origen.idUsuarioDestino(),
                origen.fecha(),
                origen.observacion(),
                origen.estadoInterno());
    }

    public MovimientoDto toDto(Movimiento origen) {
        if (origen == null) {
            return null;
        }
        return new MovimientoDto(
                origen.id(),
                origen.idActa(),
                origen.idEquipo(),
                origen.serieEquipo(),
                origen.empresaId(),
                origen.ubicacionOrigenId(),
                origen.ubicacionDestinoId(),
                origen.personaOrigenId(),
                origen.personaDestinoId(),
                origen.ejecutadoPorId(),
                origen.tipo(),
                origen.ubicacionOrigen(),
                origen.ubicacionDestino(),
                origen.idUsuarioOrigen(),
                origen.idUsuarioDestino(),
                origen.fecha(),
                origen.observacion(),
                origen.estadoInterno());
    }
}

