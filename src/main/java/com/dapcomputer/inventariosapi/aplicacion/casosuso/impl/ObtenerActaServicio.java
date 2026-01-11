package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;

public class ObtenerActaServicio implements ObtenerActaCasoUso {
    private final ActaRepositorio repositorio;

    public ObtenerActaServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Acta ejecutar(Integer id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Acta no encontrada"));
    }
}
