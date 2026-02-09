package com.dapcomputer.inventariosapi.infraestructura.configuracion;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.AgregarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarAdjuntosPorActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorRangoFechaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosPorEmpresaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarClientesCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposPorUbicacionCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarKardexRecienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.SincronizarMovimientoPorActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerDashboardResumenCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ActualizarActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ActualizarAdjuntoActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ActualizarMovimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.AgregarAdjuntoActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarAdjuntosPorActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorEstadoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorRangoFechaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarAdjuntoActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarKardexMovimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CrearClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CrearEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarClientesServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarEquiposServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarEquiposPorEstadoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarEquiposPorUbicacionServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarIncidenteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarMovimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarMovimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.BajaLogicaEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMantenimientosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMantenimientosPorSerieCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.IniciarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CompletarMantenimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarMantenimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMantenimientosServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMantenimientosPorSerieServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerMantenimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ActualizarMantenimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarMantenimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.IniciarMantenimientoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CompletarMantenimientoServicio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ActualizarIncidenteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarIncidenteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerIncidenteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ActualizarPerifericoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CrearPerifericoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CrearUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarPerifericosPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarPerifericosPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarPerifericosServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarUsuariosServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerPerifericoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarUsuariosPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarUsuariosPorEmpresaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarPerifericoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarKardexRecienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.SincronizarMovimientoPorActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerDashboardResumenServicio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaItemRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IPersonaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IUbicacionRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IEmpresaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.KardexRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasosUsoConfig {
    @Bean
    public RegistrarActaCasoUso registrarActaCasoUso(ActaRepositorio repositorio, MovimientoRepositorio movimientoRepositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso, SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso, EquipoRepositorio equipoRepositorio) {
        return new RegistrarActaServicio(repositorio, movimientoRepositorio, registrarKardexMovimientoCasoUso, sincronizarMovimientoPorActaCasoUso, equipoRepositorio);
    }

    @Bean
    public ListarActasCasoUso listarActasCasoUso(ActaRepositorio repositorio) {
        return new ListarActasServicio(repositorio);
    }

    @Bean
    public ObtenerActaCasoUso obtenerActaCasoUso(ActaRepositorio repositorio) {
        return new ObtenerActaServicio(repositorio);
    }

    @Bean
    public ActualizarActaCasoUso actualizarActaCasoUso(ActaRepositorio repositorio, ActaItemRepositorio actaItemRepositorio, SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso) {
        return new ActualizarActaServicio(repositorio, actaItemRepositorio, sincronizarMovimientoPorActaCasoUso);
    }

    @Bean
    public EliminarActaCasoUso eliminarActaCasoUso(ActaRepositorio actaRepositorio, ActaAdjuntoRepositorio adjuntoRepositorio, ActaItemRepositorio actaItemRepositorio, MovimientoRepositorio movimientoRepositorio) {
        return new EliminarActaServicio(actaRepositorio, adjuntoRepositorio, actaItemRepositorio, movimientoRepositorio);
    }

    @Bean
    public ListarActasPorEstadoCasoUso listarActasPorEstadoCasoUso(ActaRepositorio repositorio) {
        return new ListarActasPorEstadoServicio(repositorio);
    }

    @Bean
    public ListarActasPorRangoFechaCasoUso listarActasPorRangoCasoUso(ActaRepositorio repositorio) {
        return new ListarActasPorRangoFechaServicio(repositorio);
    }

    @Bean
    public ListarActasPorClienteCasoUso listarActasPorClienteCasoUso(ActaRepositorio repositorio) {
        return new ListarActasPorClienteServicio(repositorio);
    }

    @Bean
    public ListarActasPorUsuarioCasoUso listarActasPorUsuarioCasoUso(ActaRepositorio repositorio) {
        return new ListarActasPorUsuarioServicio(repositorio);
    }

    @Bean
    public RegistrarMovimientoCasoUso registrarMovimientoCasoUso(MovimientoRepositorio repositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso, BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso) {
        return new RegistrarMovimientoServicio(repositorio, registrarKardexMovimientoCasoUso, bajaLogicaEquipoCasoUso);
    }

    @Bean
    public ListarMovimientosCasoUso listarMovimientosCasoUso(MovimientoRepositorio repositorio) {
        return new ListarMovimientosServicio(repositorio);
    }

    @Bean
    public ActualizarMovimientoCasoUso actualizarMovimientoCasoUso(MovimientoRepositorio repositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso, BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso) {
        return new ActualizarMovimientoServicio(repositorio, registrarKardexMovimientoCasoUso, bajaLogicaEquipoCasoUso);
    }

    @Bean
    public EliminarMovimientoCasoUso eliminarMovimientoCasoUso(MovimientoRepositorio repositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso) {
        return new EliminarMovimientoServicio(repositorio, registrarKardexMovimientoCasoUso);
    }

    @Bean
    public ListarMovimientosPorEquipoCasoUso listarMovimientosPorEquipoCasoUso(MovimientoRepositorio repositorio) {
        return new ListarMovimientosPorEquipoServicio(repositorio);
    }

    @Bean
    public ListarMovimientosPorUsuarioCasoUso listarMovimientosPorUsuarioCasoUso(MovimientoRepositorio repositorio) {
        return new ListarMovimientosPorUsuarioServicio(repositorio);
    }

    @Bean
    public RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso(KardexRepositorio kardexRepositorio, UsuarioRepositorio usuarioRepositorio) {
        return new RegistrarKardexMovimientoServicio(kardexRepositorio, usuarioRepositorio);
    }

    @Bean
    public ListarKardexRecienteCasoUso listarKardexRecienteCasoUso(KardexRepositorio kardexRepositorio, MovimientoRepositorio movimientoRepositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso) {
        return new ListarKardexRecienteServicio(kardexRepositorio, movimientoRepositorio, registrarKardexMovimientoCasoUso);
    }

    @Bean
    public SincronizarMovimientoPorActaCasoUso sincronizarMovimientoPorActaCasoUso(MovimientoRepositorio movimientoRepositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso, EquipoRepositorio equipoRepositorio, IUbicacionRepositorio ubicacionRepositorio) {
        return new SincronizarMovimientoPorActaServicio(movimientoRepositorio, registrarKardexMovimientoCasoUso, equipoRepositorio, ubicacionRepositorio);
    }

    @Bean
    public ObtenerDashboardResumenCasoUso obtenerDashboardResumenCasoUso(ActaRepositorio actaRepositorio, MovimientoRepositorio movimientoRepositorio, ListarKardexRecienteCasoUso listarKardexRecienteCasoUso) {
        return new ObtenerDashboardResumenServicio(actaRepositorio, movimientoRepositorio, listarKardexRecienteCasoUso);
    }

    @Bean
    public AgregarAdjuntoActaCasoUso agregarAdjuntoActaCasoUso(ActaAdjuntoRepositorio repositorio) {
        return new AgregarAdjuntoActaServicio(repositorio);
    }

    @Bean
    public ListarAdjuntosPorActaCasoUso listarAdjuntosPorActaCasoUso(ActaAdjuntoRepositorio repositorio) {
        return new ListarAdjuntosPorActaServicio(repositorio);
    }

    @Bean
    public ActualizarAdjuntoActaCasoUso actualizarAdjuntoActaCasoUso(ActaAdjuntoRepositorio repositorio) {
        return new ActualizarAdjuntoActaServicio(repositorio);
    }

    @Bean
    public EliminarAdjuntoActaCasoUso eliminarAdjuntoActaCasoUso(ActaAdjuntoRepositorio repositorio) {
        return new EliminarAdjuntoActaServicio(repositorio);
    }

    @Bean
    public RegistrarIncidenteCasoUso registrarIncidenteCasoUso(IncidenteRepositorio incidenteRepositorio, EquipoRepositorio equipoRepositorio) {
        return new RegistrarIncidenteServicio(incidenteRepositorio, equipoRepositorio);
    }

    @Bean
    public ListarIncidentesPorClienteCasoUso listarIncidentesPorClienteCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ListarIncidentesPorClienteServicio(incidenteRepositorio);
    }

    @Bean
    public ListarIncidentesCasoUso listarIncidentesCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ListarIncidentesServicio(incidenteRepositorio);
    }

    @Bean
    public ListarIncidentesPorEquipoCasoUso listarIncidentesPorEquipoCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ListarIncidentesPorEquipoServicio(incidenteRepositorio);
    }

    @Bean
    public ListarIncidentesPorUsuarioCasoUso listarIncidentesPorUsuarioCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ListarIncidentesPorUsuarioServicio(incidenteRepositorio);
    }

    @Bean
    public ObtenerIncidenteCasoUso obtenerIncidenteCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ObtenerIncidenteServicio(incidenteRepositorio);
    }

    @Bean
    public ActualizarIncidenteCasoUso actualizarIncidenteCasoUso(IncidenteRepositorio incidenteRepositorio, EquipoRepositorio equipoRepositorio) {
        return new ActualizarIncidenteServicio(incidenteRepositorio, equipoRepositorio);
    }

    @Bean
    public EliminarIncidenteCasoUso eliminarIncidenteCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new EliminarIncidenteServicio(incidenteRepositorio);
    }

    @Bean
    public CrearClienteCasoUso crearClienteCasoUso(ClienteRepositorio repositorio) {
        return new CrearClienteServicio(repositorio);
    }

    @Bean
    public ListarClientesCasoUso listarClientesCasoUso(ClienteRepositorio repositorio) {
        return new ListarClientesServicio(repositorio);
    }

    @Bean
    public ObtenerClienteCasoUso obtenerClienteCasoUso(ClienteRepositorio repositorio) {
        return new ObtenerClienteServicio(repositorio);
    }

    @Bean
    public EliminarClienteCasoUso eliminarClienteCasoUso(ClienteRepositorio repositorio, EquipoRepositorio equipoRepositorio, UsuarioRepositorio usuarioRepositorio, MantenimientoRepositorio mantenimientoRepositorio, IncidenteRepositorio incidenteRepositorio, PerifericoRepositorio perifericoRepositorio, ActaRepositorio actaRepositorio, MovimientoRepositorio movimientoRepositorio, ActaAdjuntoRepositorio actaAdjuntoRepositorio, ActaItemRepositorio actaItemRepositorio, IPersonaRepositorio personaRepositorio, IUbicacionRepositorio ubicacionRepositorio, IEmpresaRepositorio empresaRepositorio, BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso) {
        return new EliminarClienteServicio(repositorio, equipoRepositorio, usuarioRepositorio, mantenimientoRepositorio, incidenteRepositorio, perifericoRepositorio, actaRepositorio, movimientoRepositorio, actaAdjuntoRepositorio, actaItemRepositorio, personaRepositorio, ubicacionRepositorio, empresaRepositorio, bajaLogicaEquipoCasoUso);
    }

    @Bean
    public EliminarEquipoCasoUso eliminarEquipoCasoUso(EquipoRepositorio repositorio) {
        return new EliminarEquipoServicio(repositorio);
    }

    @Bean
    public CrearEquipoCasoUso crearEquipoCasoUso(EquipoRepositorio repositorio, ActaRepositorio actaRepositorio, RegistrarActaCasoUso registrarActaCasoUso, ActualizarActaCasoUso actualizarActaCasoUso) {
        return new CrearEquipoServicio(repositorio, actaRepositorio, registrarActaCasoUso, actualizarActaCasoUso);
    }

    @Bean
    public ListarEquiposCasoUso listarEquiposCasoUso(EquipoRepositorio repositorio) {
        return new ListarEquiposServicio(repositorio);
    }

    @Bean
    public ListarEquiposPorEstadoCasoUso listarEquiposPorEstadoCasoUso(EquipoRepositorio repositorio) {
        return new ListarEquiposPorEstadoServicio(repositorio);
    }

    @Bean
    public ListarEquiposPorUbicacionCasoUso listarEquiposPorUbicacionCasoUso(EquipoRepositorio repositorio) {
        return new ListarEquiposPorUbicacionServicio(repositorio);
    }

    @Bean
    public ObtenerEquipoCasoUso obtenerEquipoCasoUso(EquipoRepositorio repositorio) {
        return new ObtenerEquipoServicio(repositorio);
    }

    @Bean
    public BajaLogicaEquipoCasoUso bajaLogicaEquipoCasoUso(EquipoRepositorio equipoRepositorio, MantenimientoRepositorio mantenimientoRepositorio, IncidenteRepositorio incidenteRepositorio, PerifericoRepositorio perifericoRepositorio, ActaRepositorio actaRepositorio, MovimientoRepositorio movimientoRepositorio, ActaAdjuntoRepositorio actaAdjuntoRepositorio, ActaItemRepositorio actaItemRepositorio) {
        return new BajaLogicaEquipoServicio(equipoRepositorio, mantenimientoRepositorio, incidenteRepositorio, perifericoRepositorio, actaRepositorio, movimientoRepositorio, actaAdjuntoRepositorio, actaItemRepositorio);
    }

    @Bean
    public CrearUsuarioCasoUso crearUsuarioCasoUso(UsuarioRepositorio repositorio) {
        return new CrearUsuarioServicio(repositorio);
    }

    @Bean
    public ListarUsuariosCasoUso listarUsuariosCasoUso(UsuarioRepositorio repositorio) {
        return new ListarUsuariosServicio(repositorio);
    }

    @Bean
    public ListarUsuariosPorClienteCasoUso listarUsuariosPorClienteCasoUso(UsuarioRepositorio repositorio) {
        return new ListarUsuariosPorClienteServicio(repositorio);
    }

    @Bean
    public ListarUsuariosPorEmpresaCasoUso listarUsuariosPorEmpresaCasoUso(UsuarioRepositorio repositorio) {
        return new ListarUsuariosPorEmpresaServicio(repositorio);
    }

    @Bean
    public ObtenerUsuarioCasoUso obtenerUsuarioCasoUso(UsuarioRepositorio repositorio) {
        return new ObtenerUsuarioServicio(repositorio);
    }

    @Bean
    public EliminarUsuarioCasoUso eliminarUsuarioCasoUso(UsuarioRepositorio repositorio) {
        return new EliminarUsuarioServicio(repositorio);
    }

    @Bean
    public CrearPerifericoCasoUso crearPerifericoCasoUso(PerifericoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        return new CrearPerifericoServicio(repositorio, equipoRepositorio);
    }

    @Bean
    public ActualizarPerifericoCasoUso actualizarPerifericoCasoUso(PerifericoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        return new ActualizarPerifericoServicio(repositorio, equipoRepositorio);
    }

    @Bean
    public ObtenerPerifericoCasoUso obtenerPerifericoCasoUso(PerifericoRepositorio repositorio) {
        return new ObtenerPerifericoServicio(repositorio);
    }

    @Bean
    public ListarPerifericosCasoUso listarPerifericosCasoUso(PerifericoRepositorio repositorio) {
        return new ListarPerifericosServicio(repositorio);
    }

    @Bean
    public ListarPerifericosPorEquipoCasoUso listarPerifericosPorEquipoCasoUso(PerifericoRepositorio repositorio) {
        return new ListarPerifericosPorEquipoServicio(repositorio);
    }

    @Bean
    public ListarPerifericosPorClienteCasoUso listarPerifericosPorClienteCasoUso(PerifericoRepositorio repositorio) {
        return new ListarPerifericosPorClienteServicio(repositorio);
    }

    @Bean
    public EliminarPerifericoCasoUso eliminarPerifericoCasoUso(PerifericoRepositorio repositorio) {
        return new EliminarPerifericoServicio(repositorio);
    }

    
    @Bean
    public RegistrarMantenimientoCasoUso registrarMantenimientoCasoUso(MantenimientoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        return new RegistrarMantenimientoServicio(repositorio, equipoRepositorio);
    }

    @Bean
    public ListarMantenimientosCasoUso listarMantenimientosCasoUso(MantenimientoRepositorio repositorio) {
        return new ListarMantenimientosServicio(repositorio);
    }

    @Bean
    public ListarMantenimientosPorSerieCasoUso listarMantenimientosPorSerieCasoUso(MantenimientoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        return new ListarMantenimientosPorSerieServicio(repositorio, equipoRepositorio);
    }

    @Bean
    public ObtenerMantenimientoCasoUso obtenerMantenimientoCasoUso(MantenimientoRepositorio repositorio) {
        return new ObtenerMantenimientoServicio(repositorio);
    }

    @Bean
    public ActualizarMantenimientoCasoUso actualizarMantenimientoCasoUso(MantenimientoRepositorio repositorio, EquipoRepositorio equipoRepositorio) {
        return new ActualizarMantenimientoServicio(repositorio, equipoRepositorio);
    }

    @Bean
    public EliminarMantenimientoCasoUso eliminarMantenimientoCasoUso(MantenimientoRepositorio repositorio) {
        return new EliminarMantenimientoServicio(repositorio);
    }

    @Bean
    public IniciarMantenimientoCasoUso iniciarMantenimientoCasoUso(MantenimientoRepositorio repositorio) {
        return new IniciarMantenimientoServicio(repositorio);
    }

    @Bean
    public CompletarMantenimientoCasoUso completarMantenimientoCasoUso(MantenimientoRepositorio repositorio) {
        return new CompletarMantenimientoServicio(repositorio);
    }
}

