package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

public class EliminarMovimientoServicio implements EliminarMovimientoCasoUso {
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";
    private final MovimientoRepositorio repositorio;
    private final RegistrarKardexMovimientoCasoUso registrarKardex;

    public EliminarMovimientoServicio(MovimientoRepositorio repositorio, RegistrarKardexMovimientoCasoUso registrarKardex) {
        this.repositorio = repositorio;
        this.registrarKardex = registrarKardex;
    }

    @Override
    @Transactional
    public void ejecutar(Integer id) {
        var existente = repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Movimiento no encontrado"));
        repositorio.actualizarEstadoInterno(id, ESTADO_INACTIVO);
        var marcado = new com.dapcomputer.inventariosapi.dominio.entidades.Movimiento(
                existente.id(),
                existente.idActa(),
                existente.idEquipo(),
                existente.serieEquipo(),
                existente.empresaId(),
                existente.ubicacionOrigenId(),
                existente.ubicacionDestinoId(),
                existente.personaOrigenId(),
                existente.personaDestinoId(),
                existente.ejecutadoPorId(),
                existente.tipo(),
                existente.ubicacionOrigen(),
                existente.ubicacionDestino(),
                existente.idUsuarioOrigen(),
                existente.idUsuarioDestino(),
                LocalDateTime.now(),
                existente.observacion(),
                ESTADO_INACTIVO);
        registrarKardex.ejecutar(marcado, "ELIMINADO");
    }
}
