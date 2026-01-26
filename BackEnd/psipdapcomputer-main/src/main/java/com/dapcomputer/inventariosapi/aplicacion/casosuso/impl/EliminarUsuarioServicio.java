package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarUsuarioCasoUso;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;

public class EliminarUsuarioServicio implements EliminarUsuarioCasoUso {
    private final UsuarioRepositorio repositorio;

    public EliminarUsuarioServicio(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void ejecutar(Integer id) {
        repositorio.actualizarEstadoInterno(id, "INACTIVO_INTERNAL", "INACTIVO");
    }
}
