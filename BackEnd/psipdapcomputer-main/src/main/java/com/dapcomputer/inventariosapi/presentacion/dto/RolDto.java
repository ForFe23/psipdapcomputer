package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;

public record RolDto(Long id, @NotBlank String codigo, @NotBlank String nombre) {
}
