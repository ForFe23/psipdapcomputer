package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposPorUbicacionCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import java.util.List;

public class ListarEquiposPorUbicacionServicio implements ListarEquiposPorUbicacionCasoUso {
    private final EquipoRepositorio repositorio;

    public ListarEquiposPorUbicacionServicio(EquipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Equipo> ejecutar(String ubicacion) {
        return repositorio.listarPorUbicacion(ubicacion);
    }
}

