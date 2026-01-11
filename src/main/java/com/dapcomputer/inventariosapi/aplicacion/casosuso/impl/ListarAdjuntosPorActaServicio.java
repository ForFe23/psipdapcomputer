package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarAdjuntosPorActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import java.util.List;

public class ListarAdjuntosPorActaServicio implements ListarAdjuntosPorActaCasoUso {
    private final ActaAdjuntoRepositorio repositorio;

    public ListarAdjuntosPorActaServicio(ActaAdjuntoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<ActaAdjunto> ejecutar(Integer idActa) {
        return repositorio.listarPorActa(idActa);
    }
}
