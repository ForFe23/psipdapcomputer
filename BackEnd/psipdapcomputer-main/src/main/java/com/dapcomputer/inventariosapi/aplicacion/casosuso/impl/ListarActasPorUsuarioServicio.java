package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import java.util.List;

public class ListarActasPorUsuarioServicio implements ListarActasPorUsuarioCasoUso {
    private final ActaRepositorio repositorio;

    public ListarActasPorUsuarioServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Acta> ejecutar(String nombre) {
        return repositorio.listarPorUsuario(nombre);
    }
}

