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
        equipoRepositorio.buscarPorSerie(incidente.serieEquipo())
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado para la serie"));
        LocalDateTime fecha = incidente.fechaIncidente() != null ? incidente.fechaIncidente() : LocalDateTime.now();
        Incidente entrada = new Incidente(
                incidente.id(),
                incidente.serieEquipo(),
                incidente.idUsuario(),
                incidente.idCliente(),
                fecha,
                incidente.detalle(),
                incidente.costo(),
                incidente.tecnico(),
                incidente.cliente(),
                incidente.responsable());
        return repositorio.guardar(entrada);
    }
}
