package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record KardexEntrada(
        Integer movimientoId,
        Integer equipoId,
        String serieEquipo,
        Long ubicacionOrigenId,
        String ubicacionOrigen,
        Long ubicacionDestinoId,
        String ubicacionDestino,
        String ejecutadoPor,
        LocalDateTime fecha,
        TipoMovimiento tipo,
        String accion,
        String observacion) {
}
