package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearClienteCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;

public class CrearClienteServicio implements CrearClienteCasoUso {
    private final ClienteRepositorio repositorio;

    public CrearClienteServicio(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Cliente ejecutar(Cliente cliente) {
        return repositorio.guardar(cliente);
    }
}
