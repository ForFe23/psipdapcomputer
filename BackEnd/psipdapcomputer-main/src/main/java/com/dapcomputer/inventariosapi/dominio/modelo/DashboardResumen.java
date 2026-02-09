package com.dapcomputer.inventariosapi.dominio.modelo;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import java.util.List;

public record DashboardResumen(
        List<Acta> actasAbiertas,
        List<Movimiento> movimientosAbiertos,
        List<KardexEntrada> kardexReciente) {
}

