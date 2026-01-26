package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import java.time.LocalDateTime;

public class RegistrarMantenimientoServicio implements RegistrarMantenimientoCasoUso {
    private final MantenimientoRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public RegistrarMantenimientoServicio(MantenimientoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Mantenimiento ejecutar(Mantenimiento mantenimiento) {
        if (mantenimiento.equipoId() == null) {
            throw new IllegalArgumentException("Equipo requerido");
        }
        var equipo = equipoRepositorio.buscarPorId(mantenimiento.equipoId().intValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado para el id"));
        LocalDateTime fecha = mantenimiento.fechaProgramada() != null ? mantenimiento.fechaProgramada() : LocalDateTime.now();
        LocalDateTime creado = mantenimiento.creadoEn() != null ? mantenimiento.creadoEn() : LocalDateTime.now();
        String estadoInterno = mantenimiento.estadoInterno() != null ? mantenimiento.estadoInterno() : "ACTIVO_INTERNAL";
        String estado = mantenimiento.estado() != null && !mantenimiento.estado().isBlank() ? mantenimiento.estado().toUpperCase() : "PROGRAMADO";
        if (fecha.isBefore(LocalDateTime.now())) {
            estado = "COMPLETADO";
        }
        Mantenimiento entrada = new Mantenimiento(
                mantenimiento.id(),
                mantenimiento.equipoId(),
                equipo.serie(),
                mantenimiento.idCliente() != null ? mantenimiento.idCliente() : equipo.idCliente(),
                fecha,
                mantenimiento.frecuenciaDias(),
                mantenimiento.descripcion(),
                estado,
                creado,
                estadoInterno);
        return repositorio.guardar(entrada);
    }
}
