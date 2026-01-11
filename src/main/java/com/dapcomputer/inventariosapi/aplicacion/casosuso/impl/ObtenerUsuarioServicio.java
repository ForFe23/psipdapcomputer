package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerUsuarioCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

public class ObtenerUsuarioServicio implements ObtenerUsuarioCasoUso {
    private final UsuarioRepositorio repositorio;

    public ObtenerUsuarioServicio(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Usuario ejecutar(Integer id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
