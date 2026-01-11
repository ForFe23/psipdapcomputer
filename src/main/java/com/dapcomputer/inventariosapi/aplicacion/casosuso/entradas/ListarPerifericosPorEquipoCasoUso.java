package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import java.util.List;

public interface ListarPerifericosPorEquipoCasoUso {
    List<Periferico> ejecutar(EquipoId id);
}
