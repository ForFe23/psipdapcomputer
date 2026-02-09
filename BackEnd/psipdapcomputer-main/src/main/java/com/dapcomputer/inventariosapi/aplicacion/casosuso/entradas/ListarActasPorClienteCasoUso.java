package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import java.util.List;

public interface ListarActasPorClienteCasoUso {
    List<Acta> ejecutar(Integer idCliente);
}

