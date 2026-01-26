package com.dapcomputer.inventariosapi.dominio.modelo;

public record Equipo(Integer id, String serie, Long empresaId, Long ubicacionActualId, Integer asignadoAId, String marca, String modelo, String tipo, String estado, String estadoInterno, String alias) {
}
