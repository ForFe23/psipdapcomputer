package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.AgregarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;

public class AgregarAdjuntoActaServicio implements AgregarAdjuntoActaCasoUso {
    private final ActaAdjuntoRepositorio repositorio;

    public AgregarAdjuntoActaServicio(ActaAdjuntoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public ActaAdjunto ejecutar(ActaAdjunto adjunto) {
        return repositorio.guardar(adjunto);
    }
}
