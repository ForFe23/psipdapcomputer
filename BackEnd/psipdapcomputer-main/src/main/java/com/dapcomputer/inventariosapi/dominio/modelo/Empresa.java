package com.dapcomputer.inventariosapi.dominio.modelo;

public record Empresa(Long id, Long clienteId, String nombre, String estado, String estadoInterno) {
}

