package com.dapcomputer.inventariosapi.dominio.modelo;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import java.time.LocalDateTime;

public record MovimientoEquipo(Integer id, Integer equipoId, String equipoSerie, Long empresaId, Long ubicacionOrigenId, Long ubicacionDestinoId, Integer personaOrigenId, Integer personaDestinoId, TipoMovimiento tipo, LocalDateTime fecha, Integer ejecutadoPorId, Integer actaId, String observacion, String estadoInterno) {
}
