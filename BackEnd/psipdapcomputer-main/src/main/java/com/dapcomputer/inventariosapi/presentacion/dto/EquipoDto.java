package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record EquipoDto(
        Integer id,
        @NotBlank String serie,
        @NotNull Long idCliente,
        Long empresaId,
        Long ubicacionActualId,
        Integer asignadoAId,
        String marca,
        String modelo,
        @Pattern(regexp = "^[\\p{L}\\p{N} _-]{2,50}$") String tipo,
        String sistemaOperativo,
        String procesador,
        String memoria,
        String disco,
        LocalDateTime fechaCompra,
        @Pattern(regexp = "^[\\p{L}\\p{N} _-]{2,50}$") String estado,
        String estadoInterno,
        String ip,
        @Pattern(regexp = "^[\\p{L}\\p{N} _-]{2,50}$") String ubicacionUsuario,
        String departamentoUsuario,
        String nombreUsuario,
        String nombreProveedor,
        String direccionProveedor,
        String telefonoProveedor,
        String contactoProveedor,
        String cliente,
        String activo,
        String office,
        String costo,
        String factura,
        String notas,
        String ciudad,
        String nombre,
        String alias) {}
