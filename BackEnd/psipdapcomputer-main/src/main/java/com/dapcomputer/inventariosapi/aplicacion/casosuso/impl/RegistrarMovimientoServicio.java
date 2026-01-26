package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;

public class RegistrarMovimientoServicio implements RegistrarMovimientoCasoUso {
    private final MovimientoRepositorio repositorio;

    public RegistrarMovimientoServicio(MovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Movimiento ejecutar(Movimiento movimiento) {
        return repositorio.guardar(movimiento);
    }
}
