package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MovimientoDto(
        Integer id,
        Integer idActa,
        Integer idEquipo,
        @NotBlank String serieEquipo,
        @NotNull TipoMovimiento tipo,
        @NotBlank String ubicacionOrigen,
        @NotBlank String ubicacionDestino,
        Integer idUsuarioOrigen,
        Integer idUsuarioDestino,
        @NotNull LocalDateTime fecha,
        String observacion) {}
