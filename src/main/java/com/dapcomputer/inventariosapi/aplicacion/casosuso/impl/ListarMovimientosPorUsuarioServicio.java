package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.util.List;

public class ListarMovimientosPorUsuarioServicio implements ListarMovimientosPorUsuarioCasoUso {
    private final MovimientoRepositorio repositorio;

    public ListarMovimientosPorUsuarioServicio(MovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Movimiento> ejecutar(Integer idUsuario) {
        return repositorio.listarPorUsuario(idUsuario);
    }
}
