package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UbicacionDto(
        Long id,
        @NotNull Long empresaId,
        @NotBlank String nombre,
        String direccion,
        String estado,
        String estadoInterno) {
}
