package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

public class ActualizarMovimientoServicio implements ActualizarMovimientoCasoUso {
    private final MovimientoRepositorio repositorio;
    private final RegistrarKardexMovimientoCasoUso registrarKardex;
    private final BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso;

    public ActualizarMovimientoServicio(MovimientoRepositorio repositorio, RegistrarKardexMovimientoCasoUso registrarKardex, BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso) {
        this.repositorio = repositorio;
        this.registrarKardex = registrarKardex;
        this.bajaLogicaEquipoCasoUso = bajaLogicaEquipoCasoUso;
    }

    @Override
    @Transactional
    public Movimiento ejecutar(Movimiento movimiento) {
        var existente = repositorio.buscarPorId(movimiento.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Movimiento no encontrado"));

        String estadoInterno = movimiento.estadoInterno() != null ? movimiento.estadoInterno() : existente.estadoInterno();

        Movimiento actualizado = new Movimiento(
                existente.id(),
                movimiento.idActa(),
                movimiento.idEquipo(),
                movimiento.serieEquipo(),
                movimiento.empresaId(),
                movimiento.ubicacionOrigenId(),
                movimiento.ubicacionDestinoId(),
                movimiento.personaOrigenId(),
                movimiento.personaDestinoId(),
                movimiento.ejecutadoPorId(),
                movimiento.tipo(),
                movimiento.ubicacionOrigen(),
                movimiento.ubicacionDestino(),
                movimiento.idUsuarioOrigen(),
                movimiento.idUsuarioDestino(),
                movimiento.fecha() != null ? movimiento.fecha() : LocalDateTime.now(),
                movimiento.observacion(),
                estadoInterno);

        Movimiento guardado = repositorio.guardar(actualizado);
        registrarKardex.ejecutar(guardado, "ACTUALIZADO");
        if (guardado.tipo() == TipoMovimiento.RETIRO && guardado.idEquipo() != null) {
            bajaLogicaEquipoCasoUso.ejecutar(guardado.idEquipo());
        }
        return guardado;
    }
}
