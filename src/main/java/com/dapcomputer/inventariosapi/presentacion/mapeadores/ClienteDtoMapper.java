package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.presentacion.dto.ClienteDto;

public class ClienteDtoMapper {
    public Cliente toDomain(ClienteDto origen) {
        if (origen == null) {
            return null;
        }
        return new Cliente(origen.id(), origen.nombre(), origen.contrasena(), origen.email(), origen.licencia(), origen.calle(), origen.ciudad(), origen.fechaLicencia());
    }

    public ClienteDto toDto(Cliente origen) {
        if (origen == null) {
            return null;
        }
        return new ClienteDto(origen.id(), origen.nombre(), origen.contrasena(), origen.email(), origen.licencia(), origen.calle(), origen.ciudad(), origen.fechaLicencia());
    }
}
