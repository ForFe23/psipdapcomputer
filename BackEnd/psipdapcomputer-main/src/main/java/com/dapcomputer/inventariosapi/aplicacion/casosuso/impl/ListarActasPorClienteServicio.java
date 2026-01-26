package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorClienteCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import java.util.List;

public class ListarActasPorClienteServicio implements ListarActasPorClienteCasoUso {
    private final ActaRepositorio repositorio;

    public ListarActasPorClienteServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Acta> ejecutar(Integer idCliente) {
        return repositorio.listarPorCliente(idCliente);
    }
}
