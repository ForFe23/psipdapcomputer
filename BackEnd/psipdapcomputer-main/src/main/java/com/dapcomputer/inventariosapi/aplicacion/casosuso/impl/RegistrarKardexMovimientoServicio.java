package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.KardexRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import java.time.LocalDateTime;

public class RegistrarKardexMovimientoServicio implements RegistrarKardexMovimientoCasoUso {
    private final KardexRepositorio kardexRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public RegistrarKardexMovimientoServicio(KardexRepositorio kardexRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.kardexRepositorio = kardexRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public void ejecutar(Movimiento movimiento, String accion) {
        if (movimiento == null) {
            return;
        }
        String ejecutadoPor = obtenerEjecutor(movimiento.ejecutadoPorId());
        LocalDateTime fecha = movimiento.fecha() != null ? movimiento.fecha() : LocalDateTime.now();
        String accionFinal = accion != null && !accion.isBlank() ? accion : "ACTUALIZADO";
        KardexEntrada entrada = new KardexEntrada(
                movimiento.id(),
                movimiento.idEquipo(),
                movimiento.serieEquipo(),
                movimiento.ubicacionOrigenId(),
                movimiento.ubicacionOrigen(),
                movimiento.ubicacionDestinoId(),
                movimiento.ubicacionDestino(),
                ejecutadoPor,
                fecha,
                movimiento.tipo(),
                accionFinal,
                movimiento.observacion());
        kardexRepositorio.registrar(entrada);
    }

    private String obtenerEjecutor(Integer id) {
        if (id == null) {
            return null;
        }
        return usuarioRepositorio.buscarPorId(id)
                .map(this::nombreUsuario)
                .orElse(null);
    }

    private String nombreUsuario(Usuario usuario) {
        String nombres = usuario.nombres() != null ? usuario.nombres().trim() : "";
        String apellidos = usuario.apellidos() != null ? usuario.apellidos().trim() : "";
        return (nombres + " " + apellidos).trim();
    }
}

