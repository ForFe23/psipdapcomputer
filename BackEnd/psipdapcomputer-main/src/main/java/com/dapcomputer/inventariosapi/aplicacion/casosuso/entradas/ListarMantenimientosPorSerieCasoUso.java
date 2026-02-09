package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import java.util.List;

public interface ListarMantenimientosPorSerieCasoUso {
    List<Mantenimiento> ejecutar(String serieEquipo);
}

