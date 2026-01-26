package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaItem;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActaDtoMapper {
    @Mapping(target = "items", expression = "java(toDomainItems(origen))")
    Acta toDomain(ActaDto origen);

    @Mapping(target = "items", expression = "java(toDtoItems(origen))")
    ActaDto toDto(Acta origen);

    default List<ActaItem> toDomainItems(ActaDto origen) {
        if (origen == null || origen.items() == null) {
            return List.of();
        }
        Integer actaId = origen.id();
        return origen.items().stream()
                .map(item -> new ActaItem(item.id(), actaId, item.itemNumero(), item.tipo(), item.serie(), item.modelo(), item.observacion(), item.equipoId(), item.equipoSerie()))
                .toList();
    }

    default List<ActaItemDto> toDtoItems(Acta origen) {
        if (origen == null || origen.items() == null) {
            return List.of();
        }
        return origen.items().stream()
                .map(item -> new ActaItemDto(item.id(), item.itemNumero(), item.tipo(), item.serie(), item.modelo(), item.observacion(), item.equipoId(), item.equipoSerie()))
                .toList();
    }
}
