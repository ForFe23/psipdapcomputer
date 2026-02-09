package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;

public class ObtenerClienteServicio implements ObtenerClienteCasoUso {
    private final ClienteRepositorio repositorio;

    public ObtenerClienteServicio(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Cliente ejecutar(Long id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
    }
}

