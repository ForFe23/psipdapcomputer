package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMantenimientosCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import java.util.List;

public class ListarMantenimientosServicio implements ListarMantenimientosCasoUso {
    private final MantenimientoRepositorio repositorio;

    public ListarMantenimientosServicio(MantenimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Mantenimiento> ejecutar() {
        return repositorio.listar();
    }
}

