package com.dapcomputer.inventariosapi.presentacion.dto;

import java.util.List;

public record GlobalClienteResumenDto(
        Long id,
        String nombre,
        long empresas,
        long equipos,
        long usuarios,
        long ubicaciones,
        long actas,
        long incidentes,
        long mantenimientos,
        List<GlobalEmpresaResumenDto> detalleEmpresas) {
}

