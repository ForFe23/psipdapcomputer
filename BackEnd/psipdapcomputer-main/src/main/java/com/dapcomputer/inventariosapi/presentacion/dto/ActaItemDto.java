package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActaItemDto(
        Integer id,
        @NotNull Integer itemNumero,
        @NotBlank String tipo,
        @NotBlank String serie,
        String modelo,
        String observacion,
        @NotNull Integer equipoId,
        @NotBlank String equipoSerie) {
}
