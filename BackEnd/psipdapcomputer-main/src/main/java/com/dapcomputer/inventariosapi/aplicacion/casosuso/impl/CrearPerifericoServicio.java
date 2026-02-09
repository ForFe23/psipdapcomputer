package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;

public class CrearPerifericoServicio implements CrearPerifericoCasoUso {
    private final PerifericoRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;

    public CrearPerifericoServicio(PerifericoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
    }

    @Override
    public Periferico ejecutar(Periferico periferico) {
        if (periferico.equipoId() == null) {
            throw new IllegalArgumentException("El periférico requiere un identificador de equipo");
        }
        var equipo = equipoRepositorio.buscarPorId(periferico.equipoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));

        String serie = equipo.serie();
        Integer idCliente = equipo.idCliente() != null ? Math.toIntExact(equipo.idCliente()) : periferico.idCliente();
        String cliente = equipo.cliente() != null ? equipo.cliente() : periferico.clientePerifericos();

        Periferico entrada = new Periferico(
                periferico.id(),
                periferico.equipoId(),
                normalizar(serie),
                periferico.serieMonitor(),
                periferico.activoMonitor(),
                periferico.marcaMonitor(),
                periferico.modeloMonitor(),
                periferico.observacionMonitor(),
                periferico.serieTeclado(),
                periferico.activoTeclado(),
                periferico.marcaTeclado(),
                periferico.modeloTeclado(),
                periferico.observacionTeclado(),
                periferico.serieMouse(),
                periferico.activoMouse(),
                periferico.marcaMouse(),
                periferico.modeloMouse(),
                periferico.observacionMouse(),
                periferico.serieTelefono(),
                periferico.activoTelefono(),
                periferico.marcaTelefono(),
                periferico.modeloTelefono(),
                periferico.observacionTelefono(),
                cliente,
                idCliente,
                periferico.estadoInterno());

        if (!tieneDetalle(entrada)) {
            throw new IllegalArgumentException("Debe registrar al menos un periférico (monitor/teclado/mouse/teléfono).");
        }

        return repositorio.guardar(entrada);
    }

    private String normalizar(String serie) {
        return serie != null ? serie.trim().toUpperCase() : null;
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

    private String preferir(String nuevo, String previo) {
        return !estaVacio(nuevo) ? nuevo : previo;
    }
}

