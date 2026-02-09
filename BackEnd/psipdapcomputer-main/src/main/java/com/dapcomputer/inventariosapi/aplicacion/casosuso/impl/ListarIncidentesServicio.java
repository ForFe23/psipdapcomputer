package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import java.util.List;

public class ListarIncidentesServicio implements ListarIncidentesCasoUso {
    private final IncidenteRepositorio repositorio;

    public ListarIncidentesServicio(IncidenteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Incidente> ejecutar() {
        return repositorio.listar();
    }
}

