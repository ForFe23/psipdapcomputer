package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Rol;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    default Usuario toDomain(UsuarioJpa origen) {
        if (origen == null) {
            return null;
        }
        Rol rol = null;
        if (origen.getRol() != null) {
            rol = new Rol(origen.getRol().getId(), origen.getRol().getCodigo(), origen.getRol().getNombre());
        } else if (origen.getRolId() != null) {
            rol = new Rol(origen.getRolId(), null, null);
        }
        return new Usuario(
                origen.getIdCliente(),
                origen.getEmpresaId(),
                origen.getCedula(),
                origen.getApellidos(),
                origen.getNombres(),
                rol,
                origen.getSolfrnrf(),
                origen.getCorreo(),
                origen.getTelefono(),
                origen.getEstatus(),
                origen.getFechaRegistro(),
                origen.getId(),
                origen.getEstadoInterno());
    }

    default UsuarioJpa toJpa(Usuario origen) {
        if (origen == null) {
            return null;
        }
        UsuarioJpa destino = new UsuarioJpa();
        destino.setId(origen.id());
        destino.setIdCliente(origen.idCliente());
        destino.setEmpresaId(origen.empresaId());
        destino.setCedula(origen.cedula());
        destino.setApellidos(origen.apellidos());
        destino.setNombres(origen.nombres());
        destino.setRolId(origen.rol() != null ? origen.rol().id() : null);
        destino.setSolfrnrf(origen.solfrnrf());
        destino.setCorreo(origen.correo());
        destino.setTelefono(origen.telefono());
        destino.setEstatus(origen.estatus());
        destino.setFechaRegistro(origen.fechaRegistro());
        destino.setEstadoInterno(origen.estadoInterno());
        return destino;
    }
}
