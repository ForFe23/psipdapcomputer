package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;

public class EliminarClienteServicio implements EliminarClienteCasoUso {
    private final ClienteRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final BajaLogicaEquipoCasoUso bajaLogicaEquipo;

    public EliminarClienteServicio(ClienteRepositorio repositorio, EquipoRepositorio equipoRepositorio, UsuarioRepositorio usuarioRepositorio, BajaLogicaEquipoCasoUso bajaLogicaEquipo) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.bajaLogicaEquipo = bajaLogicaEquipo;
    }

    @Override
    public void ejecutar(Long id) {
        repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        repositorio.actualizarEstadoInterno(id, "INACTIVO_INTERNAL");
        usuarioRepositorio.actualizarEstadoInternoPorCliente(id, "INACTIVO_INTERNAL", "INACTIVO");
        equipoRepositorio.listar().stream()
                .filter(e -> id.equals(e.idCliente()))
                .filter(e -> e.id() != null && e.serie() != null)
                .forEach(e -> bajaLogicaEquipo.ejecutar(e.id()));
    }
}
