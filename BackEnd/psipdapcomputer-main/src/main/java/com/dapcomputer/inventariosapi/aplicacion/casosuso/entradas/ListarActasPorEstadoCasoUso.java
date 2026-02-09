package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import java.util.List;

public interface ListarActasPorEstadoCasoUso {
    List<Acta> ejecutar(EstadoActa estado);
}

