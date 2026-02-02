package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarKardexRecienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerDashboardResumenCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.modelo.DashboardResumen;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.util.List;

public class ObtenerDashboardResumenServicio implements ObtenerDashboardResumenCasoUso {
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";
    private static final String ESTADO_ANULADO = "ANULADO_INTERNAL";
    private static final String ESTADO_COMPLETADO = "COMPLETADO_INTERNAL";
    private final ActaRepositorio actaRepositorio;
    private final MovimientoRepositorio movimientoRepositorio;
    private final ListarKardexRecienteCasoUso listarKardexRecienteCasoUso;

    public ObtenerDashboardResumenServicio(ActaRepositorio actaRepositorio, MovimientoRepositorio movimientoRepositorio, ListarKardexRecienteCasoUso listarKardexRecienteCasoUso) {
        this.actaRepositorio = actaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
        this.listarKardexRecienteCasoUso = listarKardexRecienteCasoUso;
    }

    @Override
    public DashboardResumen ejecutar(int limiteKardex) {
        List<Acta> actas = actaRepositorio.listar().stream()
                .filter(a -> a.estado() == EstadoActa.REGISTRADA || a.estado() == EstadoActa.EMITIDA)
                .filter(a -> a.estadoInterno() == null || !a.estadoInterno().equalsIgnoreCase(ESTADO_INACTIVO))
                .toList();

        List<Movimiento> movimientos = movimientoRepositorio.listar().stream()
                .filter(this::esMovimientoAbierto)
                .toList();

        var kardex = listarKardexRecienteCasoUso.ejecutar(limiteKardex > 0 ? limiteKardex : 10);
        return new DashboardResumen(actas, movimientos, kardex);
    }

    private boolean esMovimientoAbierto(Movimiento movimiento) {
        if (movimiento == null) {
            return false;
        }
        String estado = movimiento.estadoInterno();
        if (estado != null && (estado.equalsIgnoreCase(ESTADO_INACTIVO) || estado.equalsIgnoreCase(ESTADO_ANULADO) || estado.equalsIgnoreCase(ESTADO_COMPLETADO))) {
            return false;
        }
        return true;
    }
}
