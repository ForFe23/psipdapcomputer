package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;

public class ObtenerIncidenteServicio implements ObtenerIncidenteCasoUso {
    private final IncidenteRepositorio repositorio;

    public ObtenerIncidenteServicio(IncidenteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Incidente ejecutar(Integer id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Incidente no encontrado"));
    }
}

