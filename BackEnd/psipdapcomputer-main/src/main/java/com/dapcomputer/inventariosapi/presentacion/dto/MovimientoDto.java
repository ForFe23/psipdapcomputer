package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record MovimientoDto(
        Integer id,
        Integer idActa,
        @NotNull Integer idEquipo,
        String serieEquipo,
        Long empresaId,
        Long ubicacionOrigenId,
        Long ubicacionDestinoId,
        Integer personaOrigenId,
        Integer personaDestinoId,
        Integer ejecutadoPorId,
        @NotNull TipoMovimiento tipo,
        @NotBlank @Pattern(regexp = "^[\\p{L}\\p{N} _-]{2,120}$") String ubicacionOrigen,
        @NotBlank @Pattern(regexp = "^[\\p{L}\\p{N} _-]{2,120}$") String ubicacionDestino,
        Integer idUsuarioOrigen,
        Integer idUsuarioDestino,
        @NotNull LocalDateTime fecha,
        String observacion,
        String estadoInterno) {}

