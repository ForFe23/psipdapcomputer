package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record UsuarioDto(
        Integer id,
        @NotNull Long idCliente,
        @NotBlank String cedula,
        @NotBlank String apellidos,
        @NotBlank String nombres,
        @NotBlank @Pattern(regexp = "^[\\p{L}\\p{N} _-]{2,50}$") String cargo,
        String solfrnrf,
        @Email String correo,
        String telefono,
        @NotBlank @Pattern(regexp = "^(ACTIVO|INACTIVO)$") String estatus,
        LocalDateTime fechaRegistro) {}
