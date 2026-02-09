package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import java.util.List;

public interface ListarMovimientosPorEquipoCasoUso {
    List<Movimiento> ejecutar(Integer equipoId);
}

