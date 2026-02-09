package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import java.util.List;

public class ListarActasPorEstadoServicio implements ListarActasPorEstadoCasoUso {
    private final ActaRepositorio repositorio;

    public ListarActasPorEstadoServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Acta> ejecutar(EstadoActa estado) {
        return repositorio.listarPorEstado(estado);
    }
}

