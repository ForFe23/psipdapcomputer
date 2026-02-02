package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmpresaDto(
        Long id,
        @NotNull Long clienteId,
        @NotBlank String nombre,
        String estado,
        String estadoInterno) {
}
