package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpaId;

public class PerifericoMapper {
    public Periferico toDomain(PerifericoJpa origen) {
        if (origen == null || origen.getId() == null) {
            return null;
        }
        return new Periferico(origen.getId().getId(), origen.getId().getSerieEquipo(), origen.getSerieMonitor(), origen.getActivoMonitor(), origen.getMarcaMonitor(), origen.getModeloMonitor(), origen.getObservacionMonitor(), origen.getSerieTeclado(), origen.getActivoTeclado(), origen.getMarcaTeclado(), origen.getModeloTeclado(), origen.getObservacionTeclado(), origen.getSerieMouse(), origen.getActivoMouse(), origen.getMarcaMouse(), origen.getModeloMouse(), origen.getObservacionMouse(), origen.getSerieTelefono(), origen.getActivoTelefono(), origen.getMarcaTelefono(), origen.getModeloTelefono(), origen.getObservacionTelefono(), origen.getClientePerifericos(), origen.getIdCliente());
    }

    public PerifericoJpa toJpa(Periferico origen) {
        if (origen == null) {
            return null;
        }
        PerifericoJpaId id = new PerifericoJpaId(origen.id(), origen.serieEquipo());
        return PerifericoJpa.builder()
                .id(id)
                .serieMonitor(origen.serieMonitor())
                .activoMonitor(origen.activoMonitor())
                .marcaMonitor(origen.marcaMonitor())
                .modeloMonitor(origen.modeloMonitor())
                .observacionMonitor(origen.observacionMonitor())
                .serieTeclado(origen.serieTeclado())
                .activoTeclado(origen.activoTeclado())
                .marcaTeclado(origen.marcaTeclado())
                .modeloTeclado(origen.modeloTeclado())
                .observacionTeclado(origen.observacionTeclado())
                .serieMouse(origen.serieMouse())
                .activoMouse(origen.activoMouse())
                .marcaMouse(origen.marcaMouse())
                .modeloMouse(origen.modeloMouse())
                .observacionMouse(origen.observacionMouse())
                .serieTelefono(origen.serieTelefono())
                .activoTelefono(origen.activoTelefono())
                .marcaTelefono(origen.marcaTelefono())
                .modeloTelefono(origen.modeloTelefono())
                .observacionTelefono(origen.observacionTelefono())
                .clientePerifericos(origen.clientePerifericos())
                .idCliente(origen.idCliente())
                .build();
    }

    public EquipoId aIdEquipo(PerifericoJpa origen) {
        if (origen == null || origen.getId() == null) {
            return null;
        }
        return new EquipoId(origen.getId().getId(), origen.getId().getSerieEquipo());
    }
}
