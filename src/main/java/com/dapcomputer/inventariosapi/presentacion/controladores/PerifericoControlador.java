package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.presentacion.dto.PerifericoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.PerifericoDtoMapper;
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
@RequestMapping("/api/perifericos")
public class PerifericoControlador {
    private final CrearPerifericoCasoUso crearPeriferico;
    private final ListarPerifericosCasoUso listarPerifericos;
    private final ListarPerifericosPorEquipoCasoUso listarPorEquipo;
    private final EliminarPerifericoCasoUso eliminarPeriferico;
    private final PerifericoDtoMapper mapper;

    public PerifericoControlador(CrearPerifericoCasoUso crearPeriferico, ListarPerifericosCasoUso listarPerifericos, ListarPerifericosPorEquipoCasoUso listarPorEquipo, EliminarPerifericoCasoUso eliminarPeriferico, PerifericoDtoMapper mapper) {
        this.crearPeriferico = crearPeriferico;
        this.listarPerifericos = listarPerifericos;
        this.listarPorEquipo = listarPorEquipo;
        this.eliminarPeriferico = eliminarPeriferico;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PerifericoDto> crear(@Valid @RequestBody PerifericoDto solicitud) {
        var creado = crearPeriferico.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @PutMapping("/{id}/{serie}")
    public ResponseEntity<PerifericoDto> actualizar(@PathVariable Integer id, @PathVariable String serie, @Valid @RequestBody PerifericoDto solicitud) {
        var entrada = new PerifericoDto(id, serie, solicitud.serieMonitor(), solicitud.activoMonitor(), solicitud.marcaMonitor(), solicitud.modeloMonitor(), solicitud.observacionMonitor(), solicitud.serieTeclado(), solicitud.activoTeclado(), solicitud.marcaTeclado(), solicitud.modeloTeclado(), solicitud.observacionTeclado(), solicitud.serieMouse(), solicitud.activoMouse(), solicitud.marcaMouse(), solicitud.modeloMouse(), solicitud.observacionMouse(), solicitud.serieTelefono(), solicitud.activoTelefono(), solicitud.marcaTelefono(), solicitud.modeloTelefono(), solicitud.observacionTelefono(), solicitud.clientePerifericos(), solicitud.idCliente());
        var actualizado = crearPeriferico.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @GetMapping
    public List<PerifericoDto> listar() {
        return listarPerifericos.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/equipo/serie/{serie}")
    public List<PerifericoDto> listarPorSerie(@PathVariable String serie) {
        return listarPorEquipo.ejecutar(new EquipoId(null, serie)).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/equipo/{id}/{serie}")
    public List<PerifericoDto> listarPorEquipo(@PathVariable Integer id, @PathVariable String serie) {
        return listarPorEquipo.ejecutar(new EquipoId(id, serie)).stream().map(mapper::toDto).toList();
    }

    @DeleteMapping("/{id}/{serie}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id, @PathVariable String serie) {
        eliminarPeriferico.ejecutar(new EquipoId(id, serie));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
