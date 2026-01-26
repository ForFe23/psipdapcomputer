package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import java.util.List;

public class ListarIncidentesPorEquipoServicio implements ListarIncidentesPorEquipoCasoUso {
    private final IncidenteRepositorio repositorio;

    public ListarIncidentesPorEquipoServicio(IncidenteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Incidente> ejecutar(Integer equipoId) {
        return repositorio.listarPorEquipo(equipoId);
    }
}
