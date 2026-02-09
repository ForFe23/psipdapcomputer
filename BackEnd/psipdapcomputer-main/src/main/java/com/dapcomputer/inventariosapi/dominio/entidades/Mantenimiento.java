package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record Mantenimiento(
        Integer id,
        Long equipoId,
        String serieSnapshot,
        Long idCliente,
        LocalDateTime fechaProgramada,
        Integer frecuenciaDias,
        String descripcion,
        String estado,
        LocalDateTime creadoEn,
        String estadoInterno) {
}

