package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarClienteCasoUso;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;

public class EliminarClienteServicio implements EliminarClienteCasoUso {
    private final ClienteRepositorio repositorio;

    public EliminarClienteServicio(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void ejecutar(Long id) {
        repositorio.eliminar(id);
    }
}
