package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.util.List;

public class ListarMovimientosPorEquipoServicio implements ListarMovimientosPorEquipoCasoUso {
    private final MovimientoRepositorio repositorio;

    public ListarMovimientosPorEquipoServicio(MovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Movimiento> ejecutar(Integer equipoId) {
        if (equipoId == null) {
            return List.of();
        }
        return repositorio.listarPorEquipo(equipoId);
    }
}

