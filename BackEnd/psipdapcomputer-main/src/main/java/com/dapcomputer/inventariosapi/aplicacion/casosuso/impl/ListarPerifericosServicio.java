package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import java.util.List;

public class ListarPerifericosServicio implements ListarPerifericosCasoUso {
    private final PerifericoRepositorio repositorio;

    public ListarPerifericosServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Periferico> ejecutar() {
        return repositorio.listarTodos();
    }
}

