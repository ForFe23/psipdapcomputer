package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;

public record UbicacionDto(
        Long id,
        Long clienteId,
        Long empresaId,
        @NotBlank String nombre,
        String direccion,
        String estado,
        String estadoInterno) {
}

