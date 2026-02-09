package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDate;

public record Cliente(Long id, String nombre, String contrasena, String email, String licencia, String calle, String ciudad, LocalDate fechaLicencia, String estadoInterno) {
}

