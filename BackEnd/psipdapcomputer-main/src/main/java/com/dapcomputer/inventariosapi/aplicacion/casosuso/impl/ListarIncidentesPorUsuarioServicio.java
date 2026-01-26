package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import java.util.List;

public class ListarIncidentesPorUsuarioServicio implements ListarIncidentesPorUsuarioCasoUso {
    private final IncidenteRepositorio repositorio;

    public ListarIncidentesPorUsuarioServicio(IncidenteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Incidente> ejecutar(Integer idUsuario) {
        return repositorio.listarPorUsuario(idUsuario);
    }
}
