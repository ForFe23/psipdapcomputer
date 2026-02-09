package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import java.util.List;

public interface ListarAdjuntosPorActaCasoUso {
    List<ActaAdjunto> ejecutar(Integer idActa);
}

