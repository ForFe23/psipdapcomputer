package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaItemRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import org.springframework.transaction.annotation.Transactional;

public class BajaLogicaEquipoServicio implements BajaLogicaEquipoCasoUso {
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";

    private final EquipoRepositorio equipoRepositorio;
    private final MantenimientoRepositorio mantenimientoRepositorio;
    private final IncidenteRepositorio incidenteRepositorio;
    private final PerifericoRepositorio perifericoRepositorio;
    private final ActaRepositorio actaRepositorio;
    private final MovimientoRepositorio movimientoRepositorio;
    private final ActaAdjuntoRepositorio adjuntoRepositorio;
    private final ActaItemRepositorio itemRepositorio;

    public BajaLogicaEquipoServicio(
            EquipoRepositorio equipoRepositorio,
            MantenimientoRepositorio mantenimientoRepositorio,
            IncidenteRepositorio incidenteRepositorio,
            PerifericoRepositorio perifericoRepositorio,
            ActaRepositorio actaRepositorio,
            MovimientoRepositorio movimientoRepositorio,
            ActaAdjuntoRepositorio adjuntoRepositorio,
            ActaItemRepositorio itemRepositorio) {
        this.equipoRepositorio = equipoRepositorio;
        this.mantenimientoRepositorio = mantenimientoRepositorio;
        this.incidenteRepositorio = incidenteRepositorio;
        this.perifericoRepositorio = perifericoRepositorio;
        this.actaRepositorio = actaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
        this.adjuntoRepositorio = adjuntoRepositorio;
        this.itemRepositorio = itemRepositorio;
    }

    @Override
    @Transactional
    public void ejecutar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El identificador del equipo es obligatorio");
        }

        var equipo = equipoRepositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));

        equipoRepositorio.actualizarEstadoInterno(equipo.id(), ESTADO_INACTIVO);
        mantenimientoRepositorio.actualizarEstadoPorEquipo(equipo.id().longValue(), ESTADO_INACTIVO);
        incidenteRepositorio.actualizarEstadoInternoPorEquipo(equipo.id(), ESTADO_INACTIVO);
        perifericoRepositorio.actualizarEstadoInternoPorEquipo(equipo.id(), ESTADO_INACTIVO);
        actaRepositorio.actualizarEstadoInternoPorEquipo(equipo.id(), ESTADO_INACTIVO);
        movimientoRepositorio.actualizarEstadoInternoPorEquipo(equipo.id(), ESTADO_INACTIVO);
        adjuntoRepositorio.actualizarEstadoInternoPorEquipo(equipo.id(), ESTADO_INACTIVO);
        itemRepositorio.actualizarEstadoInternoPorEquipo(equipo.id(), ESTADO_INACTIVO);
    }
}
