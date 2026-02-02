package com.dapcomputer.inventariosapi.presentacion.dto;

import java.util.List;

public record ListaResumenDto<T>(long total, List<T> items) {
}
