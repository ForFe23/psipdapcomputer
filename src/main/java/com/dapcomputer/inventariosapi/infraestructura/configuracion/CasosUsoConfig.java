package com.dapcomputer.inventariosapi.infraestructura.configuracion;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.AgregarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarAdjuntosPorActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorRangoFechaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
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
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.AgregarAdjuntoActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarAdjuntosPorActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorEstadoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorRangoFechaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarEquipoServicio;
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
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CrearPerifericoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.CrearUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarPerifericosPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarPerifericosServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarUsuariosServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ObtenerUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarUsuariosPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarPerifericoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.EliminarUsuarioServicio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasosUsoConfig {
    @Bean
    public RegistrarActaCasoUso registrarActaCasoUso(ActaRepositorio repositorio) {
        return new RegistrarActaServicio(repositorio);
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
    public RegistrarMovimientoCasoUso registrarMovimientoCasoUso(MovimientoRepositorio repositorio) {
        return new RegistrarMovimientoServicio(repositorio);
    }

    @Bean
    public ListarMovimientosCasoUso listarMovimientosCasoUso(MovimientoRepositorio repositorio) {
        return new ListarMovimientosServicio(repositorio);
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
    public AgregarAdjuntoActaCasoUso agregarAdjuntoActaCasoUso(ActaAdjuntoRepositorio repositorio) {
        return new AgregarAdjuntoActaServicio(repositorio);
    }

    @Bean
    public ListarAdjuntosPorActaCasoUso listarAdjuntosPorActaCasoUso(ActaAdjuntoRepositorio repositorio) {
        return new ListarAdjuntosPorActaServicio(repositorio);
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
    public EliminarClienteCasoUso eliminarClienteCasoUso(ClienteRepositorio repositorio) {
        return new EliminarClienteServicio(repositorio);
    }

    @Bean
    public EliminarEquipoCasoUso eliminarEquipoCasoUso(EquipoRepositorio repositorio) {
        return new EliminarEquipoServicio(repositorio);
    }

    @Bean
    public CrearEquipoCasoUso crearEquipoCasoUso(EquipoRepositorio repositorio) {
        return new CrearEquipoServicio(repositorio);
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
    public ObtenerUsuarioCasoUso obtenerUsuarioCasoUso(UsuarioRepositorio repositorio) {
        return new ObtenerUsuarioServicio(repositorio);
    }

    @Bean
    public EliminarUsuarioCasoUso eliminarUsuarioCasoUso(UsuarioRepositorio repositorio) {
        return new EliminarUsuarioServicio(repositorio);
    }

    @Bean
    public CrearPerifericoCasoUso crearPerifericoCasoUso(PerifericoRepositorio repositorio) {
        return new CrearPerifericoServicio(repositorio);
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
    public EliminarPerifericoCasoUso eliminarPerifericoCasoUso(PerifericoRepositorio repositorio) {
        return new EliminarPerifericoServicio(repositorio);
    }
}
