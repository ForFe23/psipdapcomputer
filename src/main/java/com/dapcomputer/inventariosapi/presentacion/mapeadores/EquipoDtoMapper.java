package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.presentacion.dto.EquipoDto;

public class EquipoDtoMapper {
    public Equipo toDomain(EquipoDto origen) {
        if (origen == null) {
            return null;
        }
        EquipoId id = new EquipoId(origen.id(), origen.serie());
        return new Equipo(id, origen.idCliente(), origen.marca(), origen.modelo(), origen.tipo(), origen.sistemaOperativo(), origen.procesador(), origen.memoria(), origen.disco(), origen.fechaCompra(), origen.estado(), origen.ip(), origen.ubicacionUsuario(), origen.departamentoUsuario(), origen.nombreUsuario(), origen.nombreProveedor(), origen.direccionProveedor(), origen.telefonoProveedor(), origen.contactoProveedor(), origen.cliente(), origen.activo(), origen.office(), origen.costo(), origen.factura(), origen.notas(), origen.ciudad(), origen.nombre(), origen.alias());
    }

    public EquipoDto toDto(Equipo origen) {
        if (origen == null) {
            return null;
        }
        Integer id = origen.identificador() != null ? origen.identificador().id() : null;
        String serie = origen.identificador() != null ? origen.identificador().serie() : null;
        return new EquipoDto(id, serie, origen.idCliente(), origen.marca(), origen.modelo(), origen.tipo(), origen.sistemaOperativo(), origen.procesador(), origen.memoria(), origen.disco(), origen.fechaCompra(), origen.estado(), origen.ip(), origen.ubicacionUsuario(), origen.departamentoUsuario(), origen.nombreUsuario(), origen.nombreProveedor(), origen.direccionProveedor(), origen.telefonoProveedor(), origen.contactoProveedor(), origen.cliente(), origen.activo(), origen.office(), origen.costo(), origen.factura(), origen.notas(), origen.ciudad(), origen.nombre(), origen.alias());
    }
}
