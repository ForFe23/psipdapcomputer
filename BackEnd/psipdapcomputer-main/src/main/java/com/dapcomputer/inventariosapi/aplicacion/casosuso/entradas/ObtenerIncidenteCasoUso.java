package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;

public interface ObtenerIncidenteCasoUso {
    Incidente ejecutar(Integer id);
}
