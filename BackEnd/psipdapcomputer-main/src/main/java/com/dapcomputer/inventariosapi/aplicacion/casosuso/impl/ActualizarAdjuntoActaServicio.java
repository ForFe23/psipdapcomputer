package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import org.springframework.transaction.annotation.Transactional;

public class ActualizarAdjuntoActaServicio implements ActualizarAdjuntoActaCasoUso {
    private final ActaAdjuntoRepositorio repositorio;

    public ActualizarAdjuntoActaServicio(ActaAdjuntoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional
    public ActaAdjunto ejecutar(ActaAdjunto adjunto) {
        var existente = repositorio.buscarPorId(adjunto.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Adjunto no encontrado"));

        Integer actaId = adjunto.idActa() != null ? adjunto.idActa() : existente.idActa();
        String estadoInterno = adjunto.estadoInterno() != null ? adjunto.estadoInterno() : existente.estadoInterno();

        ActaAdjunto actualizado = new ActaAdjunto(
                existente.id(),
                actaId,
                adjunto.nombre(),
                adjunto.url(),
                adjunto.tipo(),
                estadoInterno);

        return repositorio.guardar(actualizado);
    }
}
