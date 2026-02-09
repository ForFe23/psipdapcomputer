package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import com.dapcomputer.inventariosapi.presentacion.dto.UbicacionDto;
import org.springframework.stereotype.Component;

@Component
public class UbicacionDtoMapper {
    public UbicacionDto toDto(Ubicacion origen) {
        if (origen == null) {
            return null;
        }
        return new UbicacionDto(
                origen.id(),
                origen.clienteId(),
                origen.empresaId(),
                origen.nombre(),
                origen.direccion(),
                origen.estado(),
                origen.estadoInterno());
    }

    public Ubicacion toDomain(UbicacionDto origen) {
        if (origen == null) {
            return null;
        }
        return new Ubicacion(
                origen.id(),
                origen.clienteId(),
                origen.empresaId(),
                origen.nombre(),
                origen.direccion(),
                origen.estado(),
                origen.estadoInterno());
    }
}

