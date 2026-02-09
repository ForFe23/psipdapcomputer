package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ClienteDto(Long id, @NotBlank String nombre, String contrasena, @Email String email, String licencia, String calle, String ciudad, LocalDate fechaLicencia, String estadoInterno) {
}

