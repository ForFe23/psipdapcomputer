package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import java.time.LocalDateTime;

public record KardexDto(
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
