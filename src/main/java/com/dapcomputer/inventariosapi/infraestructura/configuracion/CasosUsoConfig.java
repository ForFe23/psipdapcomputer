package com.dapcomputer.inventariosapi.infraestructura.configuracion;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.AgregarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarAdjuntosPorActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.AgregarAdjuntoActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarAdjuntosPorActaServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosPorEquipoServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.ListarMovimientosPorUsuarioServicio;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.impl.RegistrarMovimientoServicio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
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
}
