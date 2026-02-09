package com.dapcomputer.inventariosapi.presentacion.dto;

import java.util.List;

public record DashboardResumenDto(
        ListaResumenDto<ResumenActaDto> actasAbiertas,
        ListaResumenDto<ResumenMovimientoDto> movimientosAbiertos,
        List<KardexDto> kardexReciente) {
}

