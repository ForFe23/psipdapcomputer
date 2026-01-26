package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import java.util.List;

public class ListarEquiposServicio implements ListarEquiposCasoUso {
    private final EquipoRepositorio repositorio;

    public ListarEquiposServicio(EquipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Equipo> ejecutar() {
        return repositorio.listar();
    }
}
