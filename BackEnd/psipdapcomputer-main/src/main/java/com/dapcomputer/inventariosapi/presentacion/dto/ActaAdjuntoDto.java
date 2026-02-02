package com.dapcomputer.inventariosapi.presentacion.dto;

import jakarta.validation.constraints.NotBlank;

public record ActaAdjuntoDto(
        Integer id,
        Integer idActa,
        @NotBlank String nombre,
        @NotBlank String url,
        @NotBlank String tipo,
        String estadoInterno) {}
