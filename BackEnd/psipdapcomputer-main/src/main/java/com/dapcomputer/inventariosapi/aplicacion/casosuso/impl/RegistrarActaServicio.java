package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.SincronizarMovimientoPorActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.time.LocalDateTime;

public class RegistrarActaServicio implements RegistrarActaCasoUso {
    private final ActaRepositorio repositorio;
    private final MovimientoRepositorio movimientoRepositorio;
    private final RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso;
    private final SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso;
    private final EquipoRepositorio equipoRepositorio;

    public RegistrarActaServicio(ActaRepositorio repositorio, MovimientoRepositorio movimientoRepositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso, SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.movimientoRepositorio = movimientoRepositorio;
        this.registrarKardexMovimientoCasoUso = registrarKardexMovimientoCasoUso;
        this.sincronizarMovimientoPorActaCasoUso = sincronizarMovimientoPorActaCasoUso;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Acta ejecutar(Acta acta) {
        EstadoActa estado = acta.estado() != null ? acta.estado() : EstadoActa.REGISTRADA;
        LocalDateTime creadoEn = acta.creadoEn() != null ? acta.creadoEn() : LocalDateTime.now();
        Acta preparado = new Acta(
                acta.id(),
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
                creadoEn,
                acta.creadoPor(),
                acta.items(),
                acta.estadoInterno());
        Acta guardado = repositorio.guardar(preparado);

        if (guardado.idEquipo() != null) {
            var equipo = equipoRepositorio.buscarPorId(guardado.idEquipo()).orElse(null);
            Long origenId = equipo != null ? equipo.ubicacionActualId() : null;
            String origenTexto = equipo != null && equipo.ubicacionUsuario() != null ? equipo.ubicacionUsuario() : guardado.ubicacionUsuario();
            Movimiento autoMovimiento = new Movimiento(
                    null,
                    guardado.id(),
                    guardado.idEquipo(),
                    guardado.equipoSerie(),
                    guardado.empresaId(),
                    origenId,
                    guardado.ubicacionId(),
                    null,
                    null,
                    null,
                    TipoMovimiento.ENTREGA,
                    origenTexto,
                    guardado.ubicacionUsuario(),
                    null,
                    null,
                    LocalDateTime.now(),
                    "AUTO_ACTA",
                    estadoMovimiento(estado, guardado.estadoInterno()));
            Movimiento creado = movimientoRepositorio.guardar(autoMovimiento);
            registrarKardexMovimientoCasoUso.ejecutar(creado, "CREADO");
        }

        sincronizarMovimientoPorActaCasoUso.ejecutar(guardado, null);
        return guardado;
    }

    private String estadoMovimiento(EstadoActa estadoActa, String estadoInternoActa) {
        if (estadoActa == EstadoActa.CERRADA) {
            return "COMPLETADO_INTERNAL";
        }
        if (estadoActa == EstadoActa.ANULADA) {
            return "ANULADO_INTERNAL";
        }
        if (estadoActa == EstadoActa.EMITIDA) {
            return "ACTIVO_INTERNAL";
        }
        return estadoInternoActa != null ? estadoInternoActa : "ACTIVO_INTERNAL";
    }
}
