package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;

public class ObtenerPerifericoServicio implements ObtenerPerifericoCasoUso {
    private final PerifericoRepositorio repositorio;

    public ObtenerPerifericoServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Periferico ejecutar(Integer id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Periférico no encontrado"));
    }
}
