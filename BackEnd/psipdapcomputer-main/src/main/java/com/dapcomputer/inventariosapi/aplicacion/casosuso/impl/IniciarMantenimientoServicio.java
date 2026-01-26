package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.IniciarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;

public class IniciarMantenimientoServicio implements IniciarMantenimientoCasoUso {

    private final MantenimientoRepositorio repositorio;

    public IniciarMantenimientoServicio(MantenimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Mantenimiento ejecutar(Integer id) {
        Mantenimiento actual = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento no encontrado"));
        Mantenimiento actualizado = new Mantenimiento(
                actual.id(),
                actual.equipoId(),
                actual.serieSnapshot(),
                actual.idCliente(),
                actual.fechaProgramada(),
                actual.frecuenciaDias(),
                actual.descripcion(),
                "EN_CURSO",
                actual.creadoEn(),
                actual.estadoInterno()
        );
        return repositorio.guardar(actualizado);
    }
}
