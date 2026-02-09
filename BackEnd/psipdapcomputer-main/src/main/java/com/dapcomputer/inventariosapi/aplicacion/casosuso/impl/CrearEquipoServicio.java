package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CrearEquipoServicio implements CrearEquipoCasoUso {
    private final EquipoRepositorio repositorio;
    private final ActaRepositorio actaRepositorio;
    private final RegistrarActaCasoUso registrarActaCasoUso;
    private final ActualizarActaCasoUso actualizarActaCasoUso;

    public CrearEquipoServicio(EquipoRepositorio repositorio, ActaRepositorio actaRepositorio, RegistrarActaCasoUso registrarActaCasoUso, ActualizarActaCasoUso actualizarActaCasoUso) {
        this.repositorio = repositorio;
        this.actaRepositorio = actaRepositorio;
        this.registrarActaCasoUso = registrarActaCasoUso;
        this.actualizarActaCasoUso = actualizarActaCasoUso;
    }

    @Override
    public Equipo ejecutar(Equipo equipo) {
        Equipo guardado = repositorio.guardar(equipo);
        sincronizarActaAuto(guardado);
        return guardado;
    }

    private void sincronizarActaAuto(Equipo equipo) {
        if (equipo == null || equipo.id() == null) return;
        try {
            var existente = actaRepositorio.buscarPorEquipo(equipo.id()).orElse(null);
            Acta acta = construirActaDesdeEquipo(equipo, existente);
            if (existente == null) {
                registrarActaCasoUso.ejecutar(acta);
            } else {
                actualizarActaCasoUso.ejecutar(acta);
            }
        } catch (Exception ignored) {
        }
    }

    private Acta construirActaDesdeEquipo(Equipo equipo, Acta existente) {
        var estado = existente != null ? existente.estado() : EstadoActa.EMITIDA;
        var codigo = existente != null ? existente.codigo() : null;
        var fechaActa = existente != null ? existente.fechaActa() : LocalDate.now();
        var creadoEn = existente != null ? existente.creadoEn() : LocalDateTime.now();
        var estadoInterno = existente != null ? existente.estadoInterno() : "ACTIVO_INTERNAL";

        return new Acta(
                existente != null ? existente.id() : null,
                codigo,
                estado,
                equipo.idCliente() != null ? equipo.idCliente().intValue() : null,
                equipo.id(),
                equipo.empresaId(),
                equipo.ubicacionActualId(),
                fechaActa,
                "Entrega automática equipo " + (equipo.serie() != null ? equipo.serie() : equipo.id()),
                equipo.nombreProveedor() != null ? equipo.nombreProveedor() : "AUTO",
                equipo.nombreUsuario() != null ? equipo.nombreUsuario() : "PENDIENTE",
                null,
                null,
                equipo.departamentoUsuario(),
                equipo.ciudad(),
                equipo.ubicacionUsuario(),
                equipo.notas(),
                equipo.tipo(),
                equipo.serie(),
                equipo.modelo(),
                creadoEn,
                "AUTO",
                null,
                estadoInterno);
    }
}

