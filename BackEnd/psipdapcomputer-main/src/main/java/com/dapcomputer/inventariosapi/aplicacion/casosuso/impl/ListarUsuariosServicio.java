package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import java.util.List;

public class ListarUsuariosServicio implements ListarUsuariosCasoUso {
    private final UsuarioRepositorio repositorio;

    public ListarUsuariosServicio(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Usuario> ejecutar() {
        return repositorio.listarTodos();
    }
}
