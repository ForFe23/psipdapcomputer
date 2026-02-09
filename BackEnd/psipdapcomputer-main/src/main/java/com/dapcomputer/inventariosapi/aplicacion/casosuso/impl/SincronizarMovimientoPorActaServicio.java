package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.SincronizarMovimientoPorActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IUbicacionRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.time.LocalDateTime;
import java.util.Objects;

public class SincronizarMovimientoPorActaServicio implements SincronizarMovimientoPorActaCasoUso {
    private static final String ESTADO_ACTIVO = "ACTIVO_INTERNAL";
    private static final String ESTADO_COMPLETADO = "COMPLETADO_INTERNAL";
    private static final String ESTADO_ANULADO = "ANULADO_INTERNAL";
    private final MovimientoRepositorio movimientoRepositorio;
    private final RegistrarKardexMovimientoCasoUso registrarKardex;
    private final EquipoRepositorio equipoRepositorio;
    private final IUbicacionRepositorio ubicacionRepositorio;

    public SincronizarMovimientoPorActaServicio(MovimientoRepositorio movimientoRepositorio, RegistrarKardexMovimientoCasoUso registrarKardex, EquipoRepositorio equipoRepositorio, IUbicacionRepositorio ubicacionRepositorio) {
        this.movimientoRepositorio = movimientoRepositorio;
        this.registrarKardex = registrarKardex;
        this.equipoRepositorio = equipoRepositorio;
        this.ubicacionRepositorio = ubicacionRepositorio;
    }

    @Override
    public void ejecutar(Acta acta, Acta actaAnterior) {
        if (acta == null || acta.id() == null) {
            return;
        }
        Movimiento movimiento = movimientoRepositorio.buscarPorActa(acta.id()).orElse(null);
        if (movimiento == null && acta.idEquipo() != null) {
            var equipo = equipoRepositorio.buscarPorId(acta.idEquipo()).orElse(null);
            Long origenId = equipo != null ? equipo.ubicacionActualId() : null;
            String origenTexto = equipo != null && equipo.ubicacionUsuario() != null ? equipo.ubicacionUsuario() : obtenerUbicacionTexto(origenId, acta.ubicacionUsuario());
            String destinoTexto = obtenerUbicacionTexto(acta.ubicacionId(), acta.ubicacionUsuario());
            Movimiento nuevo = new Movimiento(
                    null,
                    acta.id(),
                    acta.idEquipo(),
                    acta.equipoSerie(),
                    acta.empresaId(),
                    origenId,
                    acta.ubicacionId(),
                    null,
                    null,
                    null,
                    acta.estado() == EstadoActa.ANULADA ? TipoMovimiento.ANULACION : TipoMovimiento.ENTREGA,
                    origenTexto,
                    destinoTexto,
                    null,
                    null,
                    LocalDateTime.now(),
                    "AUTO_ACTA",
                    estadoDesdeActa(acta.estado(), acta.estadoInterno()));
            movimiento = movimientoRepositorio.guardar(nuevo);
            registrarKardex.ejecutar(movimiento, "CREADO");
        }
        if (movimiento == null) {
            return;
        }
        boolean destinoCambio = actaAnterior != null && !Objects.equals(acta.ubicacionId(), actaAnterior.ubicacionId()) && acta.ubicacionId() != null;
        boolean estadoDistinto = acta.estado() != null && !Objects.equals(estadoDesdeActa(acta.estado(), movimiento.estadoInterno()), movimiento.estadoInterno());
        boolean requiereActualizacion = false;

        TipoMovimiento tipo = movimiento.tipo();
        Long ubicacionDestinoId = movimiento.ubicacionDestinoId();
        String ubicacionDestino = movimiento.ubicacionDestino();
        LocalDateTime fecha = movimiento.fecha();
        String observacion = movimiento.observacion();
        String estadoInterno = movimiento.estadoInterno();

        if (destinoCambio) {
            ubicacionDestinoId = acta.ubicacionId();
            ubicacionDestino = obtenerUbicacionTexto(acta.ubicacionId(), acta.ubicacionUsuario());
            fecha = LocalDateTime.now();
            observacion = combinarObservacion(observacion, "UBICACION_ACTUALIZADA");
            requiereActualizacion = true;
        }

        if (acta.estado() == EstadoActa.CERRADA && (estadoDistinto || !ESTADO_COMPLETADO.equalsIgnoreCase(estadoInterno))) {
            estadoInterno = ESTADO_COMPLETADO;
            fecha = LocalDateTime.now();
            observacion = combinarObservacion(observacion, "CERRADO_POR_ACTA");
            requiereActualizacion = true;
        } else if (acta.estado() == EstadoActa.EMITIDA && (estadoDistinto || !ESTADO_ACTIVO.equalsIgnoreCase(estadoInterno))) {
            estadoInterno = ESTADO_ACTIVO;
            if (fecha == null) {
                fecha = LocalDateTime.now();
            }
            observacion = combinarObservacion(observacion, "EMITIDO_POR_ACTA");
            requiereActualizacion = true;
        } else if (acta.estado() == EstadoActa.ANULADA && (estadoDistinto || !ESTADO_ANULADO.equalsIgnoreCase(estadoInterno) || tipo != TipoMovimiento.ANULACION)) {
            estadoInterno = ESTADO_ANULADO;
            tipo = TipoMovimiento.ANULACION;
            fecha = LocalDateTime.now();
            observacion = combinarObservacion(observacion, "ANULADO_POR_ACTA");
            if (movimiento.ubicacionOrigenId() != null) {
                ubicacionDestinoId = movimiento.ubicacionOrigenId();
                ubicacionDestino = movimiento.ubicacionOrigen();
            }
            requiereActualizacion = true;
        }

        if (requiereActualizacion) {
            Movimiento actualizado = new Movimiento(
                    movimiento.id(),
                    movimiento.idActa(),
                    movimiento.idEquipo(),
                    movimiento.serieEquipo(),
                    movimiento.empresaId() != null ? movimiento.empresaId() : acta.empresaId(),
                    movimiento.ubicacionOrigenId(),
                    ubicacionDestinoId,
                    movimiento.personaOrigenId(),
                    movimiento.personaDestinoId(),
                    movimiento.ejecutadoPorId(),
                    tipo,
                    movimiento.ubicacionOrigen(),
                    ubicacionDestino,
                    movimiento.idUsuarioOrigen(),
                    movimiento.idUsuarioDestino(),
                    fecha != null ? fecha : LocalDateTime.now(),
                    observacion,
                    estadoInterno != null ? estadoInterno : ESTADO_ACTIVO);
            Movimiento guardado = movimientoRepositorio.guardar(actualizado);
            registrarKardex.ejecutar(guardado, "SINCRONIZADO");
        }
    }

    private String obtenerUbicacionTexto(Long ubicacionId, String alterno) {
        if (ubicacionId != null) {
            return ubicacionRepositorio.buscarPorId(ubicacionId).map(u -> u.nombre() != null ? u.nombre() : alterno).orElse(alterno);
        }
        return alterno;
    }

    private String combinarObservacion(String actual, String nuevo) {
        if (nuevo == null || nuevo.isBlank()) {
            return actual;
        }
        if (actual == null || actual.isBlank()) {
            return nuevo;
        }
        if (actual.contains(nuevo)) {
            return actual;
        }
        return actual + " | " + nuevo;
    }

    private String estadoDesdeActa(EstadoActa estadoActa, String actual) {
        if (estadoActa == EstadoActa.CERRADA) {
            return ESTADO_COMPLETADO;
        }
        if (estadoActa == EstadoActa.ANULADA) {
            return ESTADO_ANULADO;
        }
        if (estadoActa == EstadoActa.EMITIDA) {
            return ESTADO_ACTIVO;
        }
        return actual != null ? actual : ESTADO_ACTIVO;
    }
}

