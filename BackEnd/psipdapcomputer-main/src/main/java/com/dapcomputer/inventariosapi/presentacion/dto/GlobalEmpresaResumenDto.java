package com.dapcomputer.inventariosapi.presentacion.dto;

public record GlobalEmpresaResumenDto(
        Long id,
        Long clienteId,
        String nombre,
        long equipos,
        long usuarios,
        long ubicaciones,
        long actas,
        long incidentes,
        long mantenimientos) {
}

