package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import java.util.List;

public class ListarActasServicio implements ListarActasCasoUso {
    private final ActaRepositorio repositorio;

    public ListarActasServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Acta> ejecutar() {
        return repositorio.listar();
    }
}

