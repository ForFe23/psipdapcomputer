package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import java.util.List;

public class ListarEquiposPorEstadoServicio implements ListarEquiposPorEstadoCasoUso {
    private final EquipoRepositorio repositorio;

    public ListarEquiposPorEstadoServicio(EquipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Equipo> ejecutar(String estado) {
        return repositorio.listarPorEstado(estado);
    }
}
