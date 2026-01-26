package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;

public class ObtenerEquipoServicio implements ObtenerEquipoCasoUso {
    private final EquipoRepositorio repositorio;

    public ObtenerEquipoServicio(EquipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Equipo ejecutar(Integer id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));
    }
}
