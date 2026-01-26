package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import java.util.List;

public class ListarUsuariosPorClienteServicio implements ListarUsuariosPorClienteCasoUso {
    private final UsuarioRepositorio repositorio;

    public ListarUsuariosPorClienteServicio(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Usuario> ejecutar(Long idCliente) {
        return repositorio.listarPorCliente(idCliente);
    }
}
