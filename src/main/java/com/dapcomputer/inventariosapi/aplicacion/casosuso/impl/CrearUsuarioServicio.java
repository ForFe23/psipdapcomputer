package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearUsuarioCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;

public class CrearUsuarioServicio implements CrearUsuarioCasoUso {
    private final UsuarioRepositorio repositorio;

    public CrearUsuarioServicio(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Usuario ejecutar(Usuario usuario) {
        return repositorio.guardar(usuario);
    }
}
