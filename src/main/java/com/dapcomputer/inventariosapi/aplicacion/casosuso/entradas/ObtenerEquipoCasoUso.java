package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;

public interface ObtenerEquipoCasoUso {
    Equipo ejecutar(EquipoId id);
}
