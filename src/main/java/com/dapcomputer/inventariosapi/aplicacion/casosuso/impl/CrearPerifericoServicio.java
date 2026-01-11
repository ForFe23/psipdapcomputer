package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearPerifericoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;

public class CrearPerifericoServicio implements CrearPerifericoCasoUso {
    private final PerifericoRepositorio repositorio;

    public CrearPerifericoServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Periferico ejecutar(Periferico periferico) {
        return repositorio.guardar(periferico);
    }
}
