package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record Incidente(Integer id, Integer equipoId, Integer idUsuario, Long idCliente, LocalDateTime fechaIncidente, String detalle, String costo, String tecnico, String responsable, String estadoInterno) {
}
