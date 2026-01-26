package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record Usuario(
        Long idCliente,
        Long empresaId,
        String cedula,
        String apellidos,
        String nombres,
        Rol rol,
        String solfrnrf,
        String correo,
        String telefono,
        String estatus,
        LocalDateTime fechaRegistro,
        Integer id,
        String estadoInterno) {
}
