package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import java.time.LocalDate;
import java.util.List;

public interface ListarActasPorRangoFechaCasoUso {
    List<Acta> ejecutar(LocalDate inicio, LocalDate fin);
}

