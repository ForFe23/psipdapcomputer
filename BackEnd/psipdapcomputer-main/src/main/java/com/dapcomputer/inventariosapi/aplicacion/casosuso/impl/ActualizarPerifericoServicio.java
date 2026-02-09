package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;

public class ActualizarPerifericoServicio implements ActualizarPerifericoCasoUso {
    private final PerifericoRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public ActualizarPerifericoServicio(PerifericoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Periferico ejecutar(Periferico periferico) {
        if (periferico.id() == null) {
            throw new IllegalArgumentException("El identificador del periférico es obligatorio");
        }
        Periferico existente = repositorio.buscarPorId(periferico.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Periférico no encontrado"));

        Integer equipoId = periferico.equipoId() != null ? periferico.equipoId() : existente.equipoId();
        if (equipoId == null) {
            throw new IllegalArgumentException("Debe asociar el equipo del periférico");
        }
        var equipo = equipoRepositorio.buscarPorId(equipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));

        String serie = equipo.serie() != null ? equipo.serie() : periferico.serieEquipo();
        Integer idCliente = equipo.idCliente() != null ? Math.toIntExact(equipo.idCliente()) : existente.idCliente();
        String cliente = equipo.cliente() != null ? equipo.cliente() : existente.clientePerifericos();

        Periferico actualizado = new Periferico(
                existente.id(),
                equipoId,
                normalizar(serie != null ? serie : existente.serieEquipo()),
                preferir(periferico.serieMonitor(), existente.serieMonitor()),
                preferir(periferico.activoMonitor(), existente.activoMonitor()),
                preferir(periferico.marcaMonitor(), existente.marcaMonitor()),
                preferir(periferico.modeloMonitor(), existente.modeloMonitor()),
                preferir(periferico.observacionMonitor(), existente.observacionMonitor()),
                preferir(periferico.serieTeclado(), existente.serieTeclado()),
                preferir(periferico.activoTeclado(), existente.activoTeclado()),
                preferir(periferico.marcaTeclado(), existente.marcaTeclado()),
                preferir(periferico.modeloTeclado(), existente.modeloTeclado()),
                preferir(periferico.observacionTeclado(), existente.observacionTeclado()),
                preferir(periferico.serieMouse(), existente.serieMouse()),
                preferir(periferico.activoMouse(), existente.activoMouse()),
                preferir(periferico.marcaMouse(), existente.marcaMouse()),
                preferir(periferico.modeloMouse(), existente.modeloMouse()),
                preferir(periferico.observacionMouse(), existente.observacionMouse()),
                preferir(periferico.serieTelefono(), existente.serieTelefono()),
                preferir(periferico.activoTelefono(), existente.activoTelefono()),
                preferir(periferico.marcaTelefono(), existente.marcaTelefono()),
                preferir(periferico.modeloTelefono(), existente.modeloTelefono()),
                preferir(periferico.observacionTelefono(), existente.observacionTelefono()),
                cliente,
                idCliente,
                preferir(periferico.estadoInterno(), existente.estadoInterno()));

        if (!tieneDetalle(actualizado)) {
            throw new IllegalArgumentException("Debe registrar al menos un periférico (monitor/teclado/mouse/teléfono).");
        }

        return repositorio.guardar(actualizado);
    }

    private String normalizar(String serie) {
        return serie != null ? serie.trim().toUpperCase() : null;
    }

    private String preferir(String nuevo, String previo) {
        if (nuevo == null) {
            return previo;
        }
        String limpio = nuevo.trim();
        return limpio.isEmpty() ? previo : limpio;
    }

    private boolean tieneDetalle(Periferico periferico) {
        return !estaVacio(periferico.serieMonitor())
                || !estaVacio(periferico.activoMonitor())
                || !estaVacio(periferico.marcaMonitor())
                || !estaVacio(periferico.modeloMonitor())
                || !estaVacio(periferico.serieTeclado())
                || !estaVacio(periferico.activoTeclado())
                || !estaVacio(periferico.marcaTeclado())
                || !estaVacio(periferico.modeloTeclado())
                || !estaVacio(periferico.serieMouse())
                || !estaVacio(periferico.activoMouse())
                || !estaVacio(periferico.marcaMouse())
                || !estaVacio(periferico.modeloMouse())
                || !estaVacio(periferico.serieTelefono())
                || !estaVacio(periferico.activoTelefono())
                || !estaVacio(periferico.marcaTelefono())
                || !estaVacio(periferico.modeloTelefono());
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}

