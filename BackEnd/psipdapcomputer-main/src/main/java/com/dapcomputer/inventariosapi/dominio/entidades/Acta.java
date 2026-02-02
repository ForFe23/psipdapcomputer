package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record Acta(
        Integer id,
        String codigo,
        EstadoActa estado,
        Integer idCliente,
        Integer idEquipo,
        Long empresaId,
        Long ubicacionId,
        LocalDate fechaActa,
        String tema,
        String entregadoPor,
        String recibidoPor,
        String cargoEntrega,
        String cargoRecibe,
        String departamentoUsuario,
        String ciudadEquipo,
        String ubicacionUsuario,
        String observacionesGenerales,
        String equipoTipo,
        String equipoSerie,
        String equipoModelo,
        LocalDateTime creadoEn,
        String creadoPor,
        List<ActaItem> items,
        String estadoInterno) {
}
