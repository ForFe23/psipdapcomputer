package com.dapcomputer.inventariosapi.dominio.entidades;

import java.time.LocalDateTime;

public record Equipo(EquipoId identificador, Long idCliente, String marca, String modelo, String tipo, String sistemaOperativo, String procesador, String memoria, String disco, LocalDateTime fechaCompra, String estado, String ip, String ubicacionUsuario, String departamentoUsuario, String nombreUsuario, String nombreProveedor, String direccionProveedor, String telefonoProveedor, String contactoProveedor, String cliente, String activo, String office, String costo, String factura, String notas, String ciudad, String nombre, String alias) {
}
