package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.time.LocalDateTime;

public class RegistrarMovimientoServicio implements RegistrarMovimientoCasoUso {
    private final MovimientoRepositorio repositorio;
    private final RegistrarKardexMovimientoCasoUso registrarKardex;
    private final BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso;

    public RegistrarMovimientoServicio(MovimientoRepositorio repositorio, RegistrarKardexMovimientoCasoUso registrarKardex, BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso) {
        this.repositorio = repositorio;
        this.registrarKardex = registrarKardex;
        this.bajaLogicaEquipoCasoUso = bajaLogicaEquipoCasoUso;
    }

    @Override
    public Movimiento ejecutar(Movimiento movimiento) {
        Movimiento preparado = movimiento;
        if (movimiento != null && movimiento.fecha() == null) {
            preparado = new Movimiento(
                    movimiento.id(),
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
                    LocalDateTime.now(),
                    movimiento.observacion(),
                    movimiento.estadoInterno());
        }
        Movimiento guardado = repositorio.guardar(preparado);
        registrarKardex.ejecutar(guardado, "CREADO");
        if (guardado.tipo() == TipoMovimiento.RETIRO && guardado.idEquipo() != null) {
            bajaLogicaEquipoCasoUso.ejecutar(guardado.idEquipo());
        }
        return guardado;
    }
}
