package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EquipoDto(Integer id, @NotBlank String serie, @NotNull Long idCliente, String marca, String modelo, String tipo, String sistemaOperativo, String procesador, String memoria, String disco, LocalDateTime fechaCompra, String estado, String ip, String ubicacionUsuario, String departamentoUsuario, String nombreUsuario, String nombreProveedor, String direccionProveedor, String telefonoProveedor, String contactoProveedor, String cliente, String activo, String office, String costo, String factura, String notas, String ciudad, String nombre, String alias) {
}
