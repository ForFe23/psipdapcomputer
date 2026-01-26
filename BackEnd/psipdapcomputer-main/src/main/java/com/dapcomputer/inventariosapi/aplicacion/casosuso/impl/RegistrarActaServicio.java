package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import java.time.LocalDateTime;

public class RegistrarActaServicio implements RegistrarActaCasoUso {
    private final ActaRepositorio repositorio;

    public RegistrarActaServicio(ActaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Acta ejecutar(Acta acta) {
        EstadoActa estado = acta.estado() != null ? acta.estado() : EstadoActa.REGISTRADA;
        LocalDateTime creadoEn = acta.creadoEn() != null ? acta.creadoEn() : LocalDateTime.now();
        Acta preparado = new Acta(acta.id(), acta.codigo(), estado, acta.idCliente(), acta.idEquipo(), acta.fechaActa(), acta.tema(), acta.entregadoPor(), acta.recibidoPor(), acta.cargoEntrega(), acta.cargoRecibe(), acta.departamentoUsuario(), acta.ciudadEquipo(), acta.ubicacionUsuario(), acta.observacionesGenerales(), acta.equipoTipo(), acta.equipoSerie(), acta.equipoModelo(), creadoEn, acta.creadoPor(), acta.items(), acta.estadoInterno());
        return repositorio.guardar(preparado);
    }
}
