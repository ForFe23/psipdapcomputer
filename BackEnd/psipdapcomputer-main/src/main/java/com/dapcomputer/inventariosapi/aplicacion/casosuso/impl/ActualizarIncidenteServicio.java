package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import java.time.LocalDateTime;

public class ActualizarIncidenteServicio implements ActualizarIncidenteCasoUso {
    private final IncidenteRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public ActualizarIncidenteServicio(IncidenteRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Incidente ejecutar(Incidente incidente) {
        var existente = repositorio.buscarPorId(incidente.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Incidente no encontrado"));

        if (incidente.equipoId() == null) {
            throw new IllegalArgumentException("Equipo requerido");
        }
        equipoRepositorio.buscarPorId(incidente.equipoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));

        LocalDateTime fecha = incidente.fechaIncidente() != null ? incidente.fechaIncidente() : existente.fechaIncidente();

        Incidente actualizado = new Incidente(
                existente.id(),
                incidente.equipoId(),
                incidente.idUsuario(),
                incidente.idCliente(),
                fecha,
                incidente.detalle(),
                incidente.costo(),
                incidente.tecnico(),
                incidente.responsable(),
                existente.estadoInterno());

        return repositorio.guardar(actualizado);
    }
}
