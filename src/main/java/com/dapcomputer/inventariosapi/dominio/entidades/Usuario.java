package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record Usuario(Long idCliente, String cedula, String apellidos, String nombres, CargoUsuario cargo, String solfrnrf, String correo, String telefono, String estatus, LocalDateTime fechaRegistro, Integer id) {
}
