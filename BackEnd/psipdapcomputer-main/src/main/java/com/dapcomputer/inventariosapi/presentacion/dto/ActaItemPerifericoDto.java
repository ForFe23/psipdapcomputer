package com.dapcomputer.inventariosapi.presentacion.dto;

public record ActaItemPerifericoDto(
        String id,
        Integer equipoId,
        String serieEquipo,
        Long empresaId,
        Integer idCliente,
        String serieMonitor,
        String marcaMonitor,
        String modeloMonitor,
        String observacionMonitor,
        String estadoInterno,
        String origen,
        Integer actaId,
        String actaCodigo,
        Integer itemNumero) {
}

