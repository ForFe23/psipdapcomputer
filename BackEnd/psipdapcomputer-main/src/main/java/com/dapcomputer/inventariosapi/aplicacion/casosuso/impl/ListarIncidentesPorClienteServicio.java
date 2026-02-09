package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorClienteCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import java.util.List;

public class ListarIncidentesPorClienteServicio implements ListarIncidentesPorClienteCasoUso {
    private final IncidenteRepositorio repositorio;

    public ListarIncidentesPorClienteServicio(IncidenteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Incidente> ejecutar(Long idCliente) {
        return repositorio.listarPorCliente(idCliente);
    }
}

