package com.dapcomputer.inventariosapi.dominio.modelo;

import com.dapcomputer.inventariosapi.dominio.entidades.CargoUsuario;

public record Persona(Integer id, Long empresaId, String cedula, String apellidos, String nombres, String correo, String telefono, CargoUsuario cargo, String estadoInterno) {
}
