package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import java.util.List;

public interface ListarMovimientosPorUsuarioCasoUso {
    List<Movimiento> ejecutar(Integer idUsuario);
}
