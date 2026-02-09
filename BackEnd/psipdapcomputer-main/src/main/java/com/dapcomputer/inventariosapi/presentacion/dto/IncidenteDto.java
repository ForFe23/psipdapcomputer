package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record IncidenteDto(
        Integer id,
        Integer equipoId,
        String serieEquipo,
        @NotNull Integer idUsuario,
        @NotNull Long idCliente,
        @NotNull LocalDateTime fechaIncidente,
        @NotBlank String detalle,
        String costo,
        String tecnico,
        String responsable,
        String estadoInterno) {}

