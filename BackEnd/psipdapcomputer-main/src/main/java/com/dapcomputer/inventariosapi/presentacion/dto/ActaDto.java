package com.dapcomputer.inventariosapi.presentacion.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;

public record ActaDto(
        Integer id,
        String codigo,
        @NotNull EstadoActa estado,
        String estadoInterno,
        @NotNull Integer idCliente,
        @NotNull Integer idEquipo,
        Long empresaId,
        Long ubicacionId,
        @NotNull LocalDate fechaActa,
        @NotBlank String tema,
        @NotBlank String entregadoPor,
        @NotBlank String recibidoPor,
        String cargoEntrega,
        String cargoRecibe,
        String departamentoUsuario,
        String ciudadEquipo,
        String ubicacionUsuario,
        String observacionesGenerales,
        String equipoTipo,
        @NotBlank String equipoSerie,
        String equipoModelo,
        LocalDateTime creadoEn,
        String creadoPor,
        @Valid List<ActaItemDto> items) {
}

