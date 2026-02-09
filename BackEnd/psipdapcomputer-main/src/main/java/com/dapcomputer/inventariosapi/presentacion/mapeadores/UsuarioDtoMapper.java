package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Rol;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.presentacion.dto.UsuarioDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {
    default Usuario toDomain(UsuarioDto origen) {
        if (origen == null) {
            return null;
        }
        Rol rol = origen.rolId() != null || origen.rolCodigo() != null
                ? new Rol(origen.rolId() != null ? origen.rolId().longValue() : null, origen.rolCodigo(), null, null)
                : null;
        return new Usuario(
                origen.idCliente(),
                origen.empresaId(),
                origen.cedula(),
                origen.apellidos(),
                origen.nombres(),
                rol,
                origen.solfrnrf(),
                origen.correo(),
                origen.telefono(),
                origen.estatus(),
                origen.fechaRegistro(),
                origen.id(),
                origen.estadoInterno());
    }

    default UsuarioDto toDto(Usuario origen) {
        if (origen == null) {
            return null;
        }
        Integer rolId = origen.rol() != null && origen.rol().id() != null ? origen.rol().id().intValue() : null;
        String rolCodigo = origen.rol() != null ? origen.rol().codigo() : null;
        return new UsuarioDto(
                origen.id(),
                origen.idCliente(),
                origen.empresaId(),
                origen.cedula(),
                origen.apellidos(),
                origen.nombres(),
                rolId,
                rolCodigo,
                origen.solfrnrf(),
                origen.correo(),
                origen.telefono(),
                origen.estatus(),
                origen.fechaRegistro(),
                origen.estadoInterno());
    }
}

