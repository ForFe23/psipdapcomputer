package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record Movimiento(Integer id, Integer idActa, Integer idEquipo, String serieEquipo, TipoMovimiento tipo, String ubicacionOrigen, String ubicacionDestino, Integer idUsuarioOrigen, Integer idUsuarioDestino, LocalDateTime fecha, String observacion) {
}
