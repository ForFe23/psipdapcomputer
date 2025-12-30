package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ClienteJpa;

public class ClienteMapper {
    public Cliente toDomain(ClienteJpa origen) {
        if (origen == null) {
            return null;
        }
        return new Cliente(origen.getId(), origen.getNombre(), origen.getContrasena(), origen.getEmail(), origen.getLicencia(), origen.getCalle(), origen.getCiudad(), origen.getFechaLicencia());
    }

    public ClienteJpa toJpa(Cliente origen) {
        if (origen == null) {
            return null;
        }
        return ClienteJpa.builder()
                .id(origen.id())
                .nombre(origen.nombre())
                .contrasena(origen.contrasena())
                .email(origen.email())
                .licencia(origen.licencia())
                .calle(origen.calle())
                .ciudad(origen.ciudad())
                .fechaLicencia(origen.fechaLicencia())
                .build();
    }
}
