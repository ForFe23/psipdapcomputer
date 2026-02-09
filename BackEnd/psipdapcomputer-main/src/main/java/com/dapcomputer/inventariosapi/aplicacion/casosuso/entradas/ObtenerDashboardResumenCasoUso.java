package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.modelo.DashboardResumen;

public interface ObtenerDashboardResumenCasoUso {
    DashboardResumen ejecutar(int limiteKardex);
}

