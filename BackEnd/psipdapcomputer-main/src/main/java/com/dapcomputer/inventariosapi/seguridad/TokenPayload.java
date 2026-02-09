package com.dapcomputer.inventariosapi.seguridad;

import java.time.Instant;
import java.util.List;

public record TokenPayload(
        Integer userId,
        String correo,
        String rol,
        Long clienteId,
        Long empresaId,
        Instant exp,
        List<String> authorities) {
}

