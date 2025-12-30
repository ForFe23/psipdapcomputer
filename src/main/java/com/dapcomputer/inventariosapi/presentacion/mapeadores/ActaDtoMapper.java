package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaItem;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemDto;
import java.util.List;

public class ActaDtoMapper {
    public Acta toDomain(ActaDto origen) {
        if (origen == null) {
            return null;
        }
        Integer actaId = origen.id();
        List<ActaItem> items = origen.items() == null ? List.of() : origen.items().stream().map(item -> toDomainItem(item, actaId)).toList();
        return new Acta(origen.id(), origen.codigo(), origen.idCliente(), origen.idEquipo(), origen.fechaActa(), origen.tema(), origen.entregadoPor(), origen.recibidoPor(), origen.cargoEntrega(), origen.cargoRecibe(), origen.departamentoUsuario(), origen.ciudadEquipo(), origen.ubicacionUsuario(), origen.observacionesGenerales(), origen.equipoTipo(), origen.equipoSerie(), origen.equipoModelo(), origen.creadoEn(), origen.creadoPor(), items);
    }

    public ActaDto toDto(Acta origen) {
        if (origen == null) {
            return null;
        }
        List<ActaItemDto> items = origen.items() == null ? List.of() : origen.items().stream().map(this::toDtoItem).toList();
        return new ActaDto(origen.id(), origen.codigo(), origen.idCliente(), origen.idEquipo(), origen.fechaActa(), origen.tema(), origen.entregadoPor(), origen.recibidoPor(), origen.cargoEntrega(), origen.cargoRecibe(), origen.departamentoUsuario(), origen.ciudadEquipo(), origen.ubicacionUsuario(), origen.observacionesGenerales(), origen.equipoTipo(), origen.equipoSerie(), origen.equipoModelo(), origen.creadoEn(), origen.creadoPor(), items);
    }

    private ActaItem toDomainItem(ActaItemDto origen, Integer actaId) {
        return new ActaItem(origen.id(), actaId, origen.itemNumero(), origen.tipo(), origen.serie(), origen.modelo(), origen.observacion());
    }

    private ActaItemDto toDtoItem(ActaItem origen) {
        return new ActaItemDto(origen.id(), origen.itemNumero(), origen.tipo(), origen.serie(), origen.modelo(), origen.observacion());
    }
}
