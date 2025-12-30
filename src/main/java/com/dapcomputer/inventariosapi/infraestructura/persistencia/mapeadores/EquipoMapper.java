package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpaId;

public class EquipoMapper {
    public Equipo toDomain(EquipoJpa origen) {
        if (origen == null) {
            return null;
        }
        EquipoId identificador = origen.getIdentificador() == null ? null : new EquipoId(origen.getIdentificador().getId(), origen.getIdentificador().getSerieEquipo());
        return new Equipo(identificador, origen.getIdCliente(), origen.getMarca(), origen.getModelo(), origen.getTipo(), origen.getSistemaOperativo(), origen.getProcesador(), origen.getMemoria(), origen.getDisco(), origen.getFechaCompra(), origen.getEstado(), origen.getIp(), origen.getUbicacionUsuario(), origen.getDepartamentoUsuario(), origen.getNombreUsuario(), origen.getNombreProveedor(), origen.getDireccionProveedor(), origen.getTelefonoProveedor(), origen.getContactoProveedor(), origen.getCliente(), origen.getActivo(), origen.getOffice(), origen.getCosto(), origen.getFactura(), origen.getNotas(), origen.getCiudad(), origen.getNombre(), origen.getAlias());
    }

    public EquipoJpa toJpa(Equipo origen) {
        if (origen == null) {
            return null;
        }
        EquipoJpaId id = origen.identificador() == null ? null : new EquipoJpaId(origen.identificador().id(), origen.identificador().serie());
        return EquipoJpa.builder()
                .identificador(id)
                .idCliente(origen.idCliente())
                .marca(origen.marca())
                .modelo(origen.modelo())
                .tipo(origen.tipo())
                .sistemaOperativo(origen.sistemaOperativo())
                .procesador(origen.procesador())
                .memoria(origen.memoria())
                .disco(origen.disco())
                .fechaCompra(origen.fechaCompra())
                .estado(origen.estado())
                .ip(origen.ip())
                .ubicacionUsuario(origen.ubicacionUsuario())
                .departamentoUsuario(origen.departamentoUsuario())
                .nombreUsuario(origen.nombreUsuario())
                .nombreProveedor(origen.nombreProveedor())
                .direccionProveedor(origen.direccionProveedor())
                .telefonoProveedor(origen.telefonoProveedor())
                .contactoProveedor(origen.contactoProveedor())
                .cliente(origen.cliente())
                .activo(origen.activo())
                .office(origen.office())
                .costo(origen.costo())
                .factura(origen.factura())
                .notas(origen.notas())
                .ciudad(origen.ciudad())
                .nombre(origen.nombre())
                .alias(origen.alias())
                .build();
    }
}
