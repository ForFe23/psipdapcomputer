package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;

public class EliminarEquipoServicio implements EliminarEquipoCasoUso {
    private final EquipoRepositorio repositorio;

    public EliminarEquipoServicio(EquipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void ejecutar(EquipoId id) {
        repositorio.eliminar(id);
    }
}
