package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;

public class ObtenerMantenimientoServicio implements ObtenerMantenimientoCasoUso {
    private final MantenimientoRepositorio repositorio;

    public ObtenerMantenimientoServicio(MantenimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Mantenimiento ejecutar(Integer id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento no encontrado"));
    }
}
