package com.dapcomputer.inventariosapi.dominio.modelo;

public record Persona(Integer id, Long clienteId, Long empresaId, String cedula, String apellidos, String nombres, String correo, String telefono, String cargo, String estadoInterno) {
}

