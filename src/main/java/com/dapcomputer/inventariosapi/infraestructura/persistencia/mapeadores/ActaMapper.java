package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaItem;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaItemJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpaId;
import java.util.Objects;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActaMapper {
    @Mapping(target = "items", source = "items")
    Acta toDomain(ActaJpa origen);

    @Mapping(target = "items", source = "items")
    ActaJpa toJpa(Acta origen);

    @Mapping(target = "actaId", expression = "java(origen.getActa() != null ? origen.getActa().getId() : null)")
    @Mapping(target = "equipoId", expression = "java(origen.getEquipo() != null ? origen.getEquipo().getId() : null)")
    @Mapping(target = "equipoSerie", expression = "java(origen.getEquipo() != null ? origen.getEquipo().getSerieEquipo() : null)")
    ActaItem toDomain(ActaItemJpa origen);

    @Mapping(target = "acta", ignore = true)
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "itemNumero", source = "itemNumero")
    ActaItemJpa toJpa(ActaItem origen);

    @AfterMapping
    default void linkActa(@MappingTarget ActaJpa destino) {
        if (destino.getItems() != null) {
            destino.getItems().stream().filter(Objects::nonNull).forEach(item -> item.setActa(destino));
        }
    }

    @AfterMapping
    default void linkEquipo(ActaItem origen, @MappingTarget ActaItemJpa destino) {
        if (origen.equipoId() != null || origen.equipoSerie() != null) {
            EquipoJpa equipo = new EquipoJpa();
            equipo.setId(origen.equipoId());
            equipo.setSerieEquipo(origen.equipoSerie());
            destino.setEquipo(equipo);
        }
    }
}
