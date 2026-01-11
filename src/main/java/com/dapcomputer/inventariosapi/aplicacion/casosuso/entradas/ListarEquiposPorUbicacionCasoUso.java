package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import java.util.List;

public interface ListarEquiposPorUbicacionCasoUso {
    List<Equipo> ejecutar(String ubicacion);
}
