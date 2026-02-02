package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaItemRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IEmpresaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IPersonaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IUbicacionRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import org.springframework.transaction.annotation.Transactional;

public class EliminarClienteServicio implements EliminarClienteCasoUso {
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";
    private final ClienteRepositorio repositorio;
    private final EquipoRepositorio equipoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final MantenimientoRepositorio mantenimientoRepositorio;
    private final IncidenteRepositorio incidenteRepositorio;
    private final PerifericoRepositorio perifericoRepositorio;
    private final ActaRepositorio actaRepositorio;
    private final MovimientoRepositorio movimientoRepositorio;
    private final ActaAdjuntoRepositorio actaAdjuntoRepositorio;
    private final ActaItemRepositorio actaItemRepositorio;
    private final IPersonaRepositorio personaRepositorio;
    private final IUbicacionRepositorio ubicacionRepositorio;
    private final IEmpresaRepositorio empresaRepositorio;
    private final BajaLogicaEquipoCasoUso bajaLogicaEquipo;

    public EliminarClienteServicio(ClienteRepositorio repositorio, EquipoRepositorio equipoRepositorio, UsuarioRepositorio usuarioRepositorio, MantenimientoRepositorio mantenimientoRepositorio, IncidenteRepositorio incidenteRepositorio, PerifericoRepositorio perifericoRepositorio, ActaRepositorio actaRepositorio, MovimientoRepositorio movimientoRepositorio, ActaAdjuntoRepositorio actaAdjuntoRepositorio, ActaItemRepositorio actaItemRepositorio, IPersonaRepositorio personaRepositorio, IUbicacionRepositorio ubicacionRepositorio, IEmpresaRepositorio empresaRepositorio, BajaLogicaEquipoCasoUso bajaLogicaEquipo) {
        this.repositorio = repositorio;
        this.equipoRepositorio = equipoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.mantenimientoRepositorio = mantenimientoRepositorio;
        this.incidenteRepositorio = incidenteRepositorio;
        this.perifericoRepositorio = perifericoRepositorio;
        this.actaRepositorio = actaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
        this.actaAdjuntoRepositorio = actaAdjuntoRepositorio;
        this.actaItemRepositorio = actaItemRepositorio;
        this.personaRepositorio = personaRepositorio;
        this.ubicacionRepositorio = ubicacionRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.bajaLogicaEquipo = bajaLogicaEquipo;
    }

    @Override
    @Transactional
    public void ejecutar(Long id) {
        repositorio.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        Integer clienteEntero = id != null ? id.intValue() : null;

        repositorio.actualizarEstadoInterno(id, ESTADO_INACTIVO);
        usuarioRepositorio.actualizarEstadoInternoPorCliente(id, ESTADO_INACTIVO, "INACTIVO");
        var empresas = empresaRepositorio.listarPorCliente(id);
        empresaRepositorio.actualizarEstadoInternoPorCliente(id, ESTADO_INACTIVO);
        empresas.forEach(e -> {
            personaRepositorio.actualizarEstadoInternoPorEmpresa(e.id(), ESTADO_INACTIVO);
            ubicacionRepositorio.actualizarEstadoInternoPorEmpresa(e.id(), ESTADO_INACTIVO);
            equipoRepositorio.actualizarEstadoInternoPorEmpresa(e.id(), ESTADO_INACTIVO);
        });
        equipoRepositorio.actualizarEstadoInternoPorCliente(id, ESTADO_INACTIVO);
        mantenimientoRepositorio.actualizarEstadoPorCliente(id, ESTADO_INACTIVO);
        incidenteRepositorio.actualizarEstadoInternoPorCliente(id, ESTADO_INACTIVO);
        perifericoRepositorio.actualizarEstadoInternoPorCliente(clienteEntero, ESTADO_INACTIVO);
        actaRepositorio.actualizarEstadoInternoPorCliente(clienteEntero, ESTADO_INACTIVO);
        movimientoRepositorio.actualizarEstadoInternoPorCliente(id, ESTADO_INACTIVO);
        actaAdjuntoRepositorio.actualizarEstadoInternoPorCliente(clienteEntero, ESTADO_INACTIVO);
        actaItemRepositorio.actualizarEstadoInternoPorCliente(clienteEntero, ESTADO_INACTIVO);

        equipoRepositorio.listarPorCliente(id).stream()
                .filter(e -> e.id() != null)
                .forEach(e -> bajaLogicaEquipo.ejecutar(e.id()));
    }
}
