package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;

public class UsuarioMapper {
    public Usuario toDomain(UsuarioJpa origen) {
        if (origen == null) {
            return null;
        }
        return new Usuario(origen.getIdCliente(), origen.getCedula(), origen.getApellidos(), origen.getNombres(), origen.getCargo(), origen.getSolfrnrf(), origen.getCorreo(), origen.getTelefono(), origen.getEstatus(), origen.getFechaRegistro(), origen.getId());
    }

    public UsuarioJpa toJpa(Usuario origen) {
        if (origen == null) {
            return null;
        }
        return UsuarioJpa.builder()
                .idCliente(origen.idCliente())
                .cedula(origen.cedula())
                .apellidos(origen.apellidos())
                .nombres(origen.nombres())
                .cargo(origen.cargo())
                .solfrnrf(origen.solfrnrf())
                .correo(origen.correo())
                .telefono(origen.telefono())
                .estatus(origen.estatus())
                .fechaRegistro(origen.fechaRegistro())
                .id(origen.id())
                .build();
    }
}
