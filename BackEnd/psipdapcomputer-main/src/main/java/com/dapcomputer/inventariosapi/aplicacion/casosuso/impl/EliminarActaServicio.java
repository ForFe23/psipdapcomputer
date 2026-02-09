package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaItemRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

public class EliminarActaServicio implements EliminarActaCasoUso {
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";
    private static final String ESTADO_ANULADO = "ANULADO_INTERNAL";

    private final ActaRepositorio actaRepositorio;
    private final ActaAdjuntoRepositorio adjuntoRepositorio;
    private final ActaItemRepositorio itemRepositorio;
    private final MovimientoRepositorio movimientoRepositorio;

    public EliminarActaServicio(ActaRepositorio actaRepositorio, ActaAdjuntoRepositorio adjuntoRepositorio, ActaItemRepositorio itemRepositorio, MovimientoRepositorio movimientoRepositorio) {
        this.actaRepositorio = actaRepositorio;
        this.adjuntoRepositorio = adjuntoRepositorio;
        this.itemRepositorio = itemRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
    }

    @Override
    @Transactional
    public void ejecutar(Integer id) {
        var acta = actaRepositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Acta no encontrada"));

        actaRepositorio.actualizarEstadoInterno(acta.id(), ESTADO_INACTIVO);
        adjuntoRepositorio.actualizarEstadoInternoPorActa(acta.id(), ESTADO_INACTIVO);
        itemRepositorio.actualizarEstadoInternoPorActa(acta.id(), ESTADO_INACTIVO);
        movimientoRepositorio.actualizarEstadoInternoPorActa(acta.id(), ESTADO_INACTIVO);

        movimientoRepositorio.buscarPorActa(acta.id()).ifPresent(mov -> {
            Movimiento revertido = new Movimiento(
                    mov.id(),
                    mov.idActa(),
                    mov.idEquipo(),
                    mov.serieEquipo(),
                    mov.empresaId(),
                    mov.ubicacionOrigenId(),
                    mov.ubicacionOrigenId(),
                    mov.personaOrigenId(),
                    mov.personaDestinoId(),
                    mov.ejecutadoPorId(),
                    TipoMovimiento.ANULACION,
                    mov.ubicacionOrigen(),
                    mov.ubicacionOrigen(),
                    mov.idUsuarioOrigen(),
                    mov.idUsuarioDestino(),
                    LocalDateTime.now(),
                    "ANULADO_POR_ELIMINACION_ACTA",
                    ESTADO_ANULADO);
            movimientoRepositorio.guardar(revertido);
        });
    }
}

