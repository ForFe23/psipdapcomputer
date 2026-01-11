package com.dapcomputer.inventariosapi.presentacion.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.presentacion.dto.EquipoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipoDtoMapper {
    @Mapping(target = "identificador", expression = "java(toId(origen.id(), origen.serie()))")
    Equipo toDomain(EquipoDto origen);

    @Mapping(target = "id", expression = "java(origen.identificador() != null ? origen.identificador().id() : null)")
    @Mapping(target = "serie", expression = "java(origen.identificador() != null ? origen.identificador().serie() : null)")
    EquipoDto toDto(Equipo origen);

    default EquipoId toId(Integer id, String serie) {
        if (id == null && serie == null) {
            return null;
        }
        return new EquipoId(id, serie);
    }
}
