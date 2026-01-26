package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;

public class EliminarMantenimientoServicio implements EliminarMantenimientoCasoUso {
    private final MantenimientoRepositorio repositorio;

    public EliminarMantenimientoServicio(MantenimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void ejecutar(Integer id) {
        repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento no encontrado"));
        repositorio.eliminar(id);
    }
}
