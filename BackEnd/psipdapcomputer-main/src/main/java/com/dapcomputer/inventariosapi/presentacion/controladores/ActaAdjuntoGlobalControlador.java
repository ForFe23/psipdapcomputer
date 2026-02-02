package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarAdjuntoActaCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaAdjuntoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.ActaAdjuntoDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actas/adjuntos")
public class ActaAdjuntoGlobalControlador {
    private final EliminarAdjuntoActaCasoUso eliminarAdjunto;
    private final ActualizarAdjuntoActaCasoUso actualizarAdjunto;
    private final ActaAdjuntoDtoMapper mapper;

    public ActaAdjuntoGlobalControlador(EliminarAdjuntoActaCasoUso eliminarAdjunto, ActualizarAdjuntoActaCasoUso actualizarAdjunto, ActaAdjuntoDtoMapper mapper) {
        this.eliminarAdjunto = eliminarAdjunto;
        this.actualizarAdjunto = actualizarAdjunto;
        this.mapper = mapper;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminarAdjunto.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActaAdjuntoDto> actualizar(@PathVariable Integer id, @Valid @RequestBody ActaAdjuntoDto solicitud) {
        var entrada = new ActaAdjuntoDto(id, solicitud.idActa(), solicitud.nombre(), solicitud.url(), solicitud.tipo(), solicitud.estadoInterno());
        var actualizado = actualizarAdjunto.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }
}
