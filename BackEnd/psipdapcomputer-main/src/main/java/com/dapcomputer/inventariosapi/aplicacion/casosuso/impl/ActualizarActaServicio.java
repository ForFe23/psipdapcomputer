package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.SincronizarMovimientoPorActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import org.springframework.transaction.annotation.Transactional;

public class ActualizarActaServicio implements ActualizarActaCasoUso {
    private final ActaRepositorio repositorio;
    private final SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso;

    public ActualizarActaServicio(ActaRepositorio repositorio, SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso) {
        this.repositorio = repositorio;
        this.sincronizarMovimientoPorActaCasoUso = sincronizarMovimientoPorActaCasoUso;
    }

    @Override
    @Transactional
    public Acta ejecutar(Acta acta) {
        var existente = repositorio.buscarPorId(acta.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Acta no encontrada"));

        EstadoActa estado = acta.estado() != null ? acta.estado() : existente.estado();
        String estadoInterno = acta.estadoInterno() != null ? acta.estadoInterno() : existente.estadoInterno();

        Acta actualizado = new Acta(
                existente.id(),
                acta.codigo(),
                estado,
                acta.idCliente(),
                acta.idEquipo(),
                acta.empresaId(),
                acta.ubicacionId(),
                acta.fechaActa(),
                acta.tema(),
                acta.entregadoPor(),
                acta.recibidoPor(),
                acta.cargoEntrega(),
                acta.cargoRecibe(),
                acta.departamentoUsuario(),
                acta.ciudadEquipo(),
                acta.ubicacionUsuario(),
                acta.observacionesGenerales(),
                acta.equipoTipo(),
                acta.equipoSerie(),
                acta.equipoModelo(),
                existente.creadoEn(),
                existente.creadoPor(),
                acta.items(),
                estadoInterno);

        Acta guardado = repositorio.guardar(actualizado);
        sincronizarMovimientoPorActaCasoUso.ejecutar(guardado, existente);
        return guardado;
    }
}
