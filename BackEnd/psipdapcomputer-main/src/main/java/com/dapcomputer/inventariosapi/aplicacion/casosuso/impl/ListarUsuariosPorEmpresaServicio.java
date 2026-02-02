package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosPorEmpresaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import java.util.List;

public class ListarUsuariosPorEmpresaServicio implements ListarUsuariosPorEmpresaCasoUso {
    private final UsuarioRepositorio repositorio;

    public ListarUsuariosPorEmpresaServicio(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Usuario> ejecutar(Long empresaId) {
        return repositorio.listarPorEmpresa(empresaId);
    }
}
