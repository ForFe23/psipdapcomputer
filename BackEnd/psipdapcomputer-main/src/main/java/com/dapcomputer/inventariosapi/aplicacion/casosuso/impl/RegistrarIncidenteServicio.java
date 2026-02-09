package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import java.time.LocalDateTime;

public class RegistrarIncidenteServicio implements RegistrarIncidenteCasoUso {
    private final IncidenteRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public RegistrarIncidenteServicio(IncidenteRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Incidente ejecutar(Incidente incidente) {
        if (incidente.equipoId() == null) {
            throw new IllegalArgumentException("Equipo requerido");
        }
        equipoRepositorio.buscarPorId(incidente.equipoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
        LocalDateTime fecha = incidente.fechaIncidente() != null ? incidente.fechaIncidente() : LocalDateTime.now();
        Incidente entrada = new Incidente(
                incidente.id(),
                incidente.equipoId(),
                incidente.idUsuario(),
                incidente.idCliente(),
                fecha,
                incidente.detalle(),
                incidente.costo(),
                incidente.tecnico(),
                incidente.responsable(),
                incidente.estadoInterno());
        return repositorio.guardar(entrada);
    }
}

