package com.dapcomputer.inventariosapi.presentacion.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;

public record ActaDto(Integer id, String codigo, EstadoActa estado, Integer idCliente, Integer idEquipo, LocalDate fechaActa, String tema, String entregadoPor, String recibidoPor, String cargoEntrega, String cargoRecibe, String departamentoUsuario, String ciudadEquipo, String ubicacionUsuario, String observacionesGenerales, String equipoTipo, String equipoSerie, String equipoModelo, LocalDateTime creadoEn, String creadoPor, List<ActaItemDto> items) {
}
