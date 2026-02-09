package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import java.util.List;

public interface ListarIncidentesPorClienteCasoUso {
    List<Incidente> ejecutar(Long idCliente);
}

