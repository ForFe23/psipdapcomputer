package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;

public interface ObtenerEquipoCasoUso {
    Equipo ejecutar(Integer id);
}

