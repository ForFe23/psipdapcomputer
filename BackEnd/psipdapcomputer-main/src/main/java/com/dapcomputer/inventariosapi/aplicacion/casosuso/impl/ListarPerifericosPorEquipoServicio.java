package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import java.util.List;

public class ListarPerifericosPorEquipoServicio implements ListarPerifericosPorEquipoCasoUso {
    private final PerifericoRepositorio repositorio;

    public ListarPerifericosPorEquipoServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
        }

    @Override
    public List<Periferico> ejecutar(Integer equipoId, String serie) {
        if (equipoId != null) {
            return repositorio.listarPorEquipo(equipoId);
        }
        if (serie != null && !serie.isBlank()) {
            return repositorio.listarPorSerie(normalizar(serie));
        }
        return List.of();
    }

    private String normalizar(String serie) {
        return serie != null ? serie.trim().toUpperCase() : null;
    }
}
