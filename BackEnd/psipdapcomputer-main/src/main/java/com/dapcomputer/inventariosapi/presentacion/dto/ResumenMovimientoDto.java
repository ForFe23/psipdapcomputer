package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import java.time.LocalDateTime;

public record ResumenMovimientoDto(Integer id, String serie, String estado, LocalDateTime fecha, TipoMovimiento tipo) {
}

