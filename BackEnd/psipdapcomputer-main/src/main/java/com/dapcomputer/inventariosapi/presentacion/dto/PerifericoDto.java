package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotNull;

public record PerifericoDto(
        Integer id,
        Integer equipoId,
        String serieEquipo,
        String serieMonitor,
        String activoMonitor,
        String marcaMonitor,
        String modeloMonitor,
        String observacionMonitor,
        String serieTeclado,
        String activoTeclado,
        String marcaTeclado,
        String modeloTeclado,
        String observacionTeclado,
        String serieMouse,
        String activoMouse,
        String marcaMouse,
        String modeloMouse,
        String observacionMouse,
        String serieTelefono,
        String activoTelefono,
        String marcaTelefono,
        String modeloTelefono,
        String observacionTelefono,
        String clientePerifericos,
        Integer idCliente,
        String estadoInterno) {}
