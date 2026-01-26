package com.dapcomputer.inventariosapi.dominio.modelo;

import java.time.LocalDateTime;

public record ActaAdjunto(Integer id, Integer actaId, String nombreArchivo, String url, String contentType, Long tamano, LocalDateTime fechaRegistro) {
}
