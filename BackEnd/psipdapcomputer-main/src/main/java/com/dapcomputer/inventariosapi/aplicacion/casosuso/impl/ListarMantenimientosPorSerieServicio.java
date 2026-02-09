package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMantenimientosPorSerieCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import java.util.Collections;
import java.util.List;

public class ListarMantenimientosPorSerieServicio implements ListarMantenimientosPorSerieCasoUso {
    private final MantenimientoRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public ListarMantenimientosPorSerieServicio(MantenimientoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public List<Mantenimiento> ejecutar(String serieEquipo) {
        var equipo = equipoRepositorio.buscarPorSerie(serieEquipo);
        if (equipo.isEmpty()) return Collections.emptyList();
        var id = equipo.get().id();
        if (id == null) return Collections.emptyList();
        return repositorio.listarPorEquipoId(id.longValue());
    }
}

