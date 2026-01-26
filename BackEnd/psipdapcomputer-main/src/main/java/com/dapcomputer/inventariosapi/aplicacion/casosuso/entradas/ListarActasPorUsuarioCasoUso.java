package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import java.util.List;

public interface ListarActasPorUsuarioCasoUso {
    List<Acta> ejecutar(String nombre);
}
