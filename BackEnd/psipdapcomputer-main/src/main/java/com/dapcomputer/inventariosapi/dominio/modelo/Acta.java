package com.dapcomputer.inventariosapi.dominio.modelo;

import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import java.time.LocalDateTime;

public record Acta(Integer id, String codigo, Long empresaId, Long ubicacionId, String tipo, EstadoActa estado, String justificacion, LocalDateTime fechaCreacion, Integer firmadoEntregoId, Integer firmadoRecibioId, Integer movimientoId, String estadoInterno) {
}

