package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.CargoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonaDto(
        Integer id,
        @NotNull Long empresaId,
        @NotBlank String cedula,
        @NotBlank String apellidos,
        @NotBlank String nombres,
        @Email String correo,
        String telefono,
        @NotNull CargoUsuario cargo,
        String estadoInterno) {
}
