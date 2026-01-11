package com.dapcomputer.inventariosapi.infraestructura.configuracion;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.AgregarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarAdjuntosPorActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorRangoFechaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.AgregarAdjuntoActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarAdjuntosPorActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorEstadoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarActasPorRangoFechaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesPorClienteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarIncidentesPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarIncidenteServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarMovimientoServicio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasosUsoConfig {
    @Bean
    public RegistrarMovimientoCasoUso registrarMovimientoCasoUso(MovimientoRepositorio repositorio) {
        return new RegistrarMovimientoServicio(repositorio);
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
    public ListarActasPorEstadoCasoUso listarActasPorEstadoCasoUso(ActaRepositorio repositorio) {
        return new ListarActasPorEstadoServicio(repositorio);
    }

    @Bean
    public ListarActasPorRangoFechaCasoUso listarActasPorRangoCasoUso(ActaRepositorio repositorio) {
        return new ListarActasPorRangoFechaServicio(repositorio);
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
    public ListarIncidentesPorEquipoCasoUso listarIncidentesPorEquipoCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ListarIncidentesPorEquipoServicio(incidenteRepositorio);
    }

    @Bean
    public ListarIncidentesPorUsuarioCasoUso listarIncidentesPorUsuarioCasoUso(IncidenteRepositorio incidenteRepositorio) {
        return new ListarIncidentesPorUsuarioServicio(incidenteRepositorio);
    }
}
