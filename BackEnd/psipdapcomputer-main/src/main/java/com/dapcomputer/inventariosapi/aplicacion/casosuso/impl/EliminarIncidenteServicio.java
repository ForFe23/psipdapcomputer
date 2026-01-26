package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;

public class EliminarIncidenteServicio implements EliminarIncidenteCasoUso {
    private final IncidenteRepositorio repositorio;

    public EliminarIncidenteServicio(IncidenteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void ejecutar(Integer id) {
        repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Incidente no encontrado"));
        repositorio.eliminar(id);
    }
}
