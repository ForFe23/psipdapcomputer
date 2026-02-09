package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import org.springframework.transaction.annotation.Transactional;

public class EliminarAdjuntoActaServicio implements EliminarAdjuntoActaCasoUso {
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";
    private final ActaAdjuntoRepositorio repositorio;

    public EliminarAdjuntoActaServicio(ActaAdjuntoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional
    public void ejecutar(Integer id) {
        repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Adjunto no encontrado"));
        repositorio.actualizarEstadoInterno(id, ESTADO_INACTIVO);
    }
}

