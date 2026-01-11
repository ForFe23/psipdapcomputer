package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import java.util.List;

public class ListarPerifericosPorEquipoServicio implements ListarPerifericosPorEquipoCasoUso {
    private final PerifericoRepositorio repositorio;

    public ListarPerifericosPorEquipoServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Periferico> ejecutar(EquipoId id) {
        if (id == null || id.id() == null) {
            return repositorio.listarPorSerie(id != null ? id.serie() : null);
        }
        return repositorio.listarPorEquipo(id);
    }
}
