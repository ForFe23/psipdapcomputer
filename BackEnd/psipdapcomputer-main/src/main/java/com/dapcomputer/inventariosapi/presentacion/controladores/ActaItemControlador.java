package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaItemJpa;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaItemSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actas/{actaId}/items")
public class ActaItemControlador {
    private static final String ESTADO_ACTIVO = "ACTIVO_INTERNAL";
    private static final String ESTADO_INACTIVO = "INACTIVO_INTERNAL";
    private final ActaSpringRepository actaRepository;
    private final ActaItemSpringRepository itemRepository;

    public ActaItemControlador(ActaSpringRepository actaRepository, ActaItemSpringRepository itemRepository) {
        this.actaRepository = actaRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<ActaItemDto> listar(@PathVariable Integer actaId) {
        return itemRepository.findByActa_Id(actaId).stream().map(this::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<ActaItemDto> crear(@PathVariable Integer actaId, @Valid @RequestBody ActaItemDto solicitud) {
        ActaJpa acta = actaRepository.findById(actaId).orElse(null);
        if (acta == null) return ResponseEntity.notFound().build();
        ActaItemJpa entidad = new ActaItemJpa();
        entidad.setActa(acta);
        entidad.setItemNumero(solicitud.itemNumero());
        entidad.setTipo(solicitud.tipo());
        entidad.setSerie(solicitud.serie());
        entidad.setModelo(solicitud.modelo());
        entidad.setObservacion(solicitud.observacion());
        entidad.setEquipoId(solicitud.equipoId());
        entidad.setEquipoSerie(solicitud.equipoSerie());
        entidad.setEstadoInterno(solicitud.estadoInterno() != null ? solicitud.estadoInterno() : ESTADO_ACTIVO);
        ActaItemJpa guardado = itemRepository.save(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(guardado));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ActaItemDto> actualizar(@PathVariable Integer actaId, @PathVariable Integer itemId, @Valid @RequestBody ActaItemDto solicitud) {
        ActaItemJpa existente = itemRepository.findById(itemId).orElse(null);
        if (existente == null || existente.getActa() == null || !existente.getActa().getId().equals(actaId)) {
            return ResponseEntity.notFound().build();
        }
        existente.setItemNumero(solicitud.itemNumero());
        existente.setTipo(solicitud.tipo());
        existente.setSerie(solicitud.serie());
        existente.setModelo(solicitud.modelo());
        existente.setObservacion(solicitud.observacion());
        existente.setEquipoId(solicitud.equipoId());
        existente.setEquipoSerie(solicitud.equipoSerie());
        existente.setEstadoInterno(solicitud.estadoInterno() != null ? solicitud.estadoInterno() : existente.getEstadoInterno());
        ActaItemJpa guardado = itemRepository.save(existente);
        return ResponseEntity.ok(toDto(guardado));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer actaId, @PathVariable Integer itemId) {
        ActaItemJpa existente = itemRepository.findById(itemId).orElse(null);
        if (existente == null || existente.getActa() == null || !existente.getActa().getId().equals(actaId)) {
            return ResponseEntity.notFound().build();
        }
        existente.setEstadoInterno(ESTADO_INACTIVO);
        itemRepository.save(existente);
        return ResponseEntity.noContent().build();
    }

    private ActaItemDto toDto(ActaItemJpa item) {
        return new ActaItemDto(
                item.getId(),
                item.getItemNumero(),
                item.getTipo(),
                item.getSerie(),
                item.getModelo(),
                item.getObservacion(),
                item.getEquipoId(),
                item.getEquipoSerie(),
                item.getEstadoInterno());
    }
}
