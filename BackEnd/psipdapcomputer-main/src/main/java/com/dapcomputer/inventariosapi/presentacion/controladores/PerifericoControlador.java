package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarPerifericoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarPerifericosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerPerifericoCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.PerifericoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.PerifericoDtoMapper;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perifericos")
public class PerifericoControlador {
    private final CrearPerifericoCasoUso crearPeriferico;
    private final ActualizarPerifericoCasoUso actualizarPeriferico;
    private final ListarPerifericosCasoUso listarPerifericos;
    private final ListarPerifericosPorEquipoCasoUso listarPorEquipo;
    private final ListarPerifericosPorClienteCasoUso listarPorCliente;
    private final ObtenerPerifericoCasoUso obtenerPeriferico;
    private final EliminarPerifericoCasoUso eliminarPeriferico;
    private final PerifericoDtoMapper mapper;
    private final EquipoRepositorio equipoRepositorio;

    public PerifericoControlador(
            CrearPerifericoCasoUso crearPeriferico,
            ActualizarPerifericoCasoUso actualizarPeriferico,
            ListarPerifericosCasoUso listarPerifericos,
            ListarPerifericosPorEquipoCasoUso listarPorEquipo,
            ListarPerifericosPorClienteCasoUso listarPorCliente,
            ObtenerPerifericoCasoUso obtenerPeriferico,
            EliminarPerifericoCasoUso eliminarPeriferico,
            PerifericoDtoMapper mapper,
            EquipoRepositorio equipoRepositorio) {
        this.crearPeriferico = crearPeriferico;
        this.actualizarPeriferico = actualizarPeriferico;
        this.listarPerifericos = listarPerifericos;
        this.listarPorEquipo = listarPorEquipo;
        this.listarPorCliente = listarPorCliente;
        this.obtenerPeriferico = obtenerPeriferico;
        this.eliminarPeriferico = eliminarPeriferico;
        this.mapper = mapper;
        this.equipoRepositorio = equipoRepositorio;
    }

    @PostMapping
    public ResponseEntity<PerifericoDto> crear(@Valid @RequestBody PerifericoDto solicitud) {
        Integer equipoId = resolverEquipoId(solicitud.equipoId(), solicitud.serieEquipo());
        if (equipoId == null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "equipoId o serieEquipo requerido");
        }
        var creado = crearPeriferico.ejecutar(new Periferico(
                solicitud.id(),
                equipoId,
                solicitud.serieEquipo(),
                solicitud.serieMonitor(),
                solicitud.activoMonitor(),
                solicitud.marcaMonitor(),
                solicitud.modeloMonitor(),
                solicitud.observacionMonitor(),
                solicitud.serieTeclado(),
                solicitud.activoTeclado(),
                solicitud.marcaTeclado(),
                solicitud.modeloTeclado(),
                solicitud.observacionTeclado(),
                solicitud.serieMouse(),
                solicitud.activoMouse(),
                solicitud.marcaMouse(),
                solicitud.modeloMouse(),
                solicitud.observacionMouse(),
                solicitud.serieTelefono(),
                solicitud.activoTelefono(),
                solicitud.marcaTelefono(),
                solicitud.modeloTelefono(),
                solicitud.observacionTelefono(),
                solicitud.clientePerifericos(),
                solicitud.idCliente(),
                solicitud.estadoInterno()));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @GetMapping
    public List<PerifericoDto> listar(
            @RequestParam(value = "equipoId", required = false) Integer equipoId,
            @RequestParam(value = "serie", required = false) String serie,
            @RequestParam(value = "clienteId", required = false) Integer clienteId) {
        var base = (equipoId != null || (serie != null && !serie.isBlank()))
                ? listarPorEquipo.ejecutar(equipoId, serie)
                : (clienteId != null ? listarPorCliente.ejecutar(clienteId) : listarPerifericos.ejecutar());

        if (clienteId != null && (equipoId != null || (serie != null && !serie.isBlank()))) {
            base = base.stream()
                    .filter(p -> p.idCliente() != null && p.idCliente().equals(clienteId))
                    .toList();
        }

        return base.stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerifericoDto> obtener(@PathVariable Integer id) {
        var encontrado = obtenerPeriferico.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerifericoDto> actualizar(@PathVariable Integer id, @Valid @RequestBody PerifericoDto solicitud) {
        Integer equipoId = resolverEquipoId(solicitud.equipoId(), solicitud.serieEquipo());
        if (equipoId == null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "equipoId o serieEquipo requerido");
        }
        var entrada = new PerifericoDto(
                id,
                equipoId,
                solicitud.serieEquipo(),
                solicitud.serieMonitor(),
                solicitud.activoMonitor(),
                solicitud.marcaMonitor(),
                solicitud.modeloMonitor(),
                solicitud.observacionMonitor(),
                solicitud.serieTeclado(),
                solicitud.activoTeclado(),
                solicitud.marcaTeclado(),
                solicitud.modeloTeclado(),
                solicitud.observacionTeclado(),
                solicitud.serieMouse(),
                solicitud.activoMouse(),
                solicitud.marcaMouse(),
                solicitud.modeloMouse(),
                solicitud.observacionMouse(),
                solicitud.serieTelefono(),
                solicitud.activoTelefono(),
                solicitud.marcaTelefono(),
                solicitud.modeloTelefono(),
                solicitud.observacionTelefono(),
                solicitud.clientePerifericos(),
                solicitud.idCliente(),
                solicitud.estadoInterno());
        var actualizado = actualizarPeriferico.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @GetMapping("/equipo/{equipoId}")
    public List<PerifericoDto> listarPorEquipo(@PathVariable String equipoId) {
        Integer id = resolverEquipoId(resolverId(equipoId), equipoId);
        if (id == null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "equipoId o serie inválido");
        }
        return listarPorEquipo.ejecutar(id, null).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/equipo/serie/{serie}")
    public List<PerifericoDto> listarPorSerie(@PathVariable String serie) {
        return listarPorEquipo.ejecutar(null, serie).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<PerifericoDto> listarPorCliente(@PathVariable Integer idCliente) {
        return listarPorCliente.ejecutar(idCliente).stream().map(mapper::toDto).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminarPeriferico.ejecutar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Integer resolverEquipoId(Integer equipoId, String serie) {
        if (equipoId != null) {
            return equipoId;
        }
        if (serie != null && !serie.isBlank()) {
            return equipoRepositorio.buscarPorSerie(serie.trim().toUpperCase()).map(e -> e.id()).orElse(null);
        }
        return null;
    }

    private Integer resolverId(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(valor);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

