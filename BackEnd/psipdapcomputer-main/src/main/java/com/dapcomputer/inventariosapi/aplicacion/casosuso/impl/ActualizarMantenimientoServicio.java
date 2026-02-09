package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import java.time.LocalDateTime;

public class ActualizarMantenimientoServicio implements ActualizarMantenimientoCasoUso {
    private final MantenimientoRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public ActualizarMantenimientoServicio(MantenimientoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Mantenimiento ejecutar(Mantenimiento mantenimiento) {
        if (mantenimiento.equipoId() == null) {
            throw new IllegalArgumentException("Equipo requerido");
        }
        var existente = repositorio.buscarPorId(mantenimiento.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento no encontrado"));

        var equipo = equipoRepositorio.buscarPorId(mantenimiento.equipoId().intValue())
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado para el id"));

        LocalDateTime fecha = mantenimiento.fechaProgramada() != null ? mantenimiento.fechaProgramada() : existente.fechaProgramada();
        String estado = mantenimiento.estado() != null && !mantenimiento.estado().isBlank() ? mantenimiento.estado().toUpperCase() : existente.estado();
        if (fecha != null && fecha.isBefore(LocalDateTime.now())) {
            estado = "COMPLETADO";
        }
        String estadoInterno = mantenimiento.estadoInterno() != null ? mantenimiento.estadoInterno() : existente.estadoInterno();
        Mantenimiento actualizado = new Mantenimiento(
                existente.id(),
                mantenimiento.equipoId(),
                equipo.serie(),
                mantenimiento.idCliente() != null ? mantenimiento.idCliente() : existente.idCliente(),
                fecha,
                mantenimiento.frecuenciaDias(),
                mantenimiento.descripcion(),
                estado,
                existente.creadoEn(),
                estadoInterno);
        return repositorio.guardar(actualizado);
    }
}

