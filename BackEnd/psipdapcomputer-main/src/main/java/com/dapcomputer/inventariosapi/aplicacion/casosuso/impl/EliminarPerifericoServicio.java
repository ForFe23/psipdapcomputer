package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;

public class EliminarPerifericoServicio implements EliminarPerifericoCasoUso {
    private final PerifericoRepositorio repositorio;

    public EliminarPerifericoServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void ejecutar(Integer id) {
        if (id == null) {
            return;
        }
        repositorio.eliminar(id);
    }
}
