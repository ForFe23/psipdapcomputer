package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import java.util.List;

public interface ListarIncidentesPorEquipoCasoUso {
    List<Incidente> ejecutar(Integer equipoId);
}

