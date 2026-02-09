package com.dapcomputer.inventariosapi.presentacion.dto;

public record AuthResponse(
        String token,
        String rol,
        Long clienteId,
        Long empresaId,
        Integer idUsuario,
        String correo) {
}

