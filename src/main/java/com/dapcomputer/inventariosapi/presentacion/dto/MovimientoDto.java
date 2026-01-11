package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import java.time.LocalDateTime;

public record MovimientoDto(Integer id, Integer idActa, Integer idEquipo, String serieEquipo, TipoMovimiento tipo, String ubicacionOrigen, String ubicacionDestino, Integer idUsuarioOrigen, Integer idUsuarioDestino, LocalDateTime fecha, String observacion) {
}
