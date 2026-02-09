package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PersonaDto(
        Integer id,
        Long clienteId,
        Long empresaId,
        @NotBlank String cedula,
        @NotBlank String apellidos,
        @NotBlank String nombres,
        @Email String correo,
        String telefono,
        @NotBlank String cargo,
        String estadoInterno) {
}

