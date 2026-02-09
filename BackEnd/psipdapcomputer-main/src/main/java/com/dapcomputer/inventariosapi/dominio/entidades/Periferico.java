package com.dapcomputer.inventariosapi.dominio.entidades;

public record Periferico(
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
        String estadoInterno) {
}

