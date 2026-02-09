package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MantenimientoDto(
        Integer id,
        @NotNull Long equipoId,
        String serieSnapshot,
        @NotNull Long idCliente,
        @NotNull LocalDateTime fechaProgramada,
        Integer frecuenciaDias,
        String descripcion,
        String estado,
        LocalDateTime creadoEn,
        String estadoInterno) {
}

