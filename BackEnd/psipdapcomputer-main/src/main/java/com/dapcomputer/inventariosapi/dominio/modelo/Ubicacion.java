package com.dapcomputer.inventariosapi.dominio.modelo;

public record Ubicacion(Long id, Long clienteId, Long empresaId, String nombre, String direccion, String estado, String estadoInterno) {
}

