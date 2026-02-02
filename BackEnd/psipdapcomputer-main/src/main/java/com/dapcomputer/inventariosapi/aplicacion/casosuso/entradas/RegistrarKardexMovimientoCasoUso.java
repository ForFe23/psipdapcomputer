package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;

public interface RegistrarKardexMovimientoCasoUso {
    void ejecutar(Movimiento movimiento, String accion);
}
