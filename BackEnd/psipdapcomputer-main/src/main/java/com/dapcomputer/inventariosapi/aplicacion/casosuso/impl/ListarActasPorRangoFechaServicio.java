package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorRangoFechaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import java.time.LocalDate;
import java.util.List;

public class ListarActasPorRangoFechaServicio implements ListarActasPorRangoFechaCasoUso {
    private final ActaRepositorio repositorio;

    public ListarActasPorRangoFechaServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Acta> ejecutar(LocalDate inicio, LocalDate fin) {
        return repositorio.listarPorRangoFecha(inicio, fin);
    }
}

