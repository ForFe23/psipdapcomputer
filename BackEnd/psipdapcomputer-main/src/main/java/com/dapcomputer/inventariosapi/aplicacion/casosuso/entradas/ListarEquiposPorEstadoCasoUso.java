package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import java.util.List;

public interface ListarEquiposPorEstadoCasoUso {
    List<Equipo> ejecutar(String estado);
}

