package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;

public class CrearEquipoServicio implements CrearEquipoCasoUso {
    private final EquipoRepositorio repositorio;

    public CrearEquipoServicio(EquipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Equipo ejecutar(Equipo equipo) {
        return repositorio.guardar(equipo);
    }
}
