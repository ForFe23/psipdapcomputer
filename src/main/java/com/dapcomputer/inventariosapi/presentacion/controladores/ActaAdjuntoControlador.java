package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.AgregarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarAdjuntosPorActaCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaAdjuntoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.ActaAdjuntoDtoMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actas/{idActa}/adjuntos")
public class ActaAdjuntoControlador {
    private final AgregarAdjuntoActaCasoUso agregarAdjunto;
    private final ListarAdjuntosPorActaCasoUso listarAdjuntos;
    private final ActaAdjuntoDtoMapper mapper = new ActaAdjuntoDtoMapper();

    public ActaAdjuntoControlador(AgregarAdjuntoActaCasoUso agregarAdjunto, ListarAdjuntosPorActaCasoUso listarAdjuntos) {
        this.agregarAdjunto = agregarAdjunto;
        this.listarAdjuntos = listarAdjuntos;
    }

    @PostMapping
    public ResponseEntity<ActaAdjuntoDto> crear(@PathVariable Integer idActa, @Valid @RequestBody ActaAdjuntoDto solicitud) {
        var entrada = new ActaAdjuntoDto(solicitud.id(), idActa, solicitud.nombre(), solicitud.url(), solicitud.tipo());
        var creado = agregarAdjunto.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(creado));
    }

    @GetMapping
    public List<ActaAdjuntoDto> listar(@PathVariable Integer idActa) {
        return mapper.toDtoList(listarAdjuntos.ejecutar(idActa));
    }
}
