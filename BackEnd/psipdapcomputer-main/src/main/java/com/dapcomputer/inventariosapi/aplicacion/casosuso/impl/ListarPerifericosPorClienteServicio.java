package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import java.util.List;

public class ListarPerifericosPorClienteServicio implements ListarPerifericosPorClienteCasoUso {
    private final PerifericoRepositorio repositorio;

    public ListarPerifericosPorClienteServicio(PerifericoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Periferico> ejecutar(Integer idCliente) {
        return idCliente == null ? List.of() : repositorio.listarPorCliente(idCliente);
    }
}
