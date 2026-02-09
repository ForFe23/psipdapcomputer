package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;

public interface ObtenerMantenimientoCasoUso {
    Mantenimiento ejecutar(Integer id);
}

