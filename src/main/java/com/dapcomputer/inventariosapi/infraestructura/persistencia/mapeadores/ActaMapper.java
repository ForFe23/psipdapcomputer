package com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.ActaItem;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaItemJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import java.util.List;
import java.util.stream.Collectors;

public class ActaMapper {
    public Acta toDomain(ActaJpa origen) {
        if (origen == null) {
            return null;
        }
        List<ActaItem> items = origen.getItems() == null ? List.of() : origen.getItems().stream().map(this::toDomainItem).toList();
        return new Acta(origen.getId(), origen.getCodigo(), origen.getEstado(), origen.getIdCliente(), origen.getIdEquipo(), origen.getFechaActa(), origen.getTema(), origen.getEntregadoPor(), origen.getRecibidoPor(), origen.getCargoEntrega(), origen.getCargoRecibe(), origen.getDepartamentoUsuario(), origen.getCiudadEquipo(), origen.getUbicacionUsuario(), origen.getObservacionesGenerales(), origen.getEquipoTipo(), origen.getEquipoSerie(), origen.getEquipoModelo(), origen.getCreadoEn(), origen.getCreadoPor(), items);
    }

    public ActaJpa toJpa(Acta origen) {
        if (origen == null) {
            return null;
        }
        ActaJpa destino = ActaJpa.builder()
                .id(origen.id())
                .codigo(origen.codigo())
                .estado(origen.estado())
                .idCliente(origen.idCliente())
                .idEquipo(origen.idEquipo())
                .fechaActa(origen.fechaActa())
                .tema(origen.tema())
                .entregadoPor(origen.entregadoPor())
                .recibidoPor(origen.recibidoPor())
                .cargoEntrega(origen.cargoEntrega())
                .cargoRecibe(origen.cargoRecibe())
                .departamentoUsuario(origen.departamentoUsuario())
                .ciudadEquipo(origen.ciudadEquipo())
                .ubicacionUsuario(origen.ubicacionUsuario())
                .observacionesGenerales(origen.observacionesGenerales())
                .equipoTipo(origen.equipoTipo())
                .equipoSerie(origen.equipoSerie())
                .equipoModelo(origen.equipoModelo())
                .creadoEn(origen.creadoEn())
                .creadoPor(origen.creadoPor())
                .build();
        List<ActaItemJpa> items = origen.items() == null ? List.of() : origen.items().stream().map(item -> toJpaItem(item, destino)).collect(Collectors.toList());
        destino.setItems(items);
        return destino;
    }

    private ActaItem toDomainItem(ActaItemJpa origen) {
        return new ActaItem(origen.getId(), origen.getActa() != null ? origen.getActa().getId() : null, origen.getItemNumero(), origen.getTipo(), origen.getSerie(), origen.getModelo(), origen.getObservacion());
    }

    private ActaItemJpa toJpaItem(ActaItem origen, ActaJpa acta) {
        ActaItemJpa destino = ActaItemJpa.builder()
                .id(origen.id())
                .acta(acta)
                .itemNumero(origen.itemNumero())
                .tipo(origen.tipo())
                .serie(origen.serie())
                .modelo(origen.modelo())
                .observacion(origen.observacion())
                .build();
        return destino;
    }
}
