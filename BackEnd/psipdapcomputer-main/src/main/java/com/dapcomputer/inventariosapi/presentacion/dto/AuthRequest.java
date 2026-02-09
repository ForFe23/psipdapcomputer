package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @Email @NotBlank String correo,
        @NotBlank String password) {
}

