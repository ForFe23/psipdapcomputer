package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.util.List;

public class ListarMovimientosServicio implements ListarMovimientosCasoUso {
    private final MovimientoRepositorio repositorio;

    public ListarMovimientosServicio(MovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Movimiento> ejecutar() {
        return repositorio.listar();
    }
}

