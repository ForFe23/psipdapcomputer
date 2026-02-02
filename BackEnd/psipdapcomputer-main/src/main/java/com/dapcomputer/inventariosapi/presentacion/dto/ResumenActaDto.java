package com.dapcomputer.inventariosapi.presentacion.dto;

import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import java.time.LocalDate;

public record ResumenActaDto(Integer id, String tema, EstadoActa estado, LocalDate fecha) {
}
