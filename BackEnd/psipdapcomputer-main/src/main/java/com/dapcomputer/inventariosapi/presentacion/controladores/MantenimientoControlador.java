package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.*;
import com.dapcomputer.inventariosapi.presentacion.dto.MantenimientoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.MantenimientoDtoMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoControlador {
    private final RegistrarMantenimientoCasoUso registrar;
    private final ListarMantenimientosCasoUso listar;
    private final ListarMantenimientosPorSerieCasoUso listarPorSerie;
    private final ObtenerMantenimientoCasoUso obtener;
    private final ActualizarMantenimientoCasoUso actualizar;
    private final EliminarMantenimientoCasoUso eliminar;
    private final IniciarMantenimientoCasoUso iniciar;
    private final CompletarMantenimientoCasoUso completar;
    private final MantenimientoDtoMapper mapper;

    public MantenimientoControlador(
            RegistrarMantenimientoCasoUso registrar,
            ListarMantenimientosCasoUso listar,
            ListarMantenimientosPorSerieCasoUso listarPorSerie,
            ObtenerMantenimientoCasoUso obtener,
            ActualizarMantenimientoCasoUso actualizar,
            EliminarMantenimientoCasoUso eliminar,
            IniciarMantenimientoCasoUso iniciar,
            CompletarMantenimientoCasoUso completar,
            MantenimientoDtoMapper mapper) {
        this.registrar = registrar;
        this.listar = listar;
        this.listarPorSerie = listarPorSerie;
        this.obtener = obtener;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
        this.iniciar = iniciar;
        this.completar = completar;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<MantenimientoDto> crear(@Valid @RequestBody MantenimientoDto dto) {
        var creado = registrar.ejecutar(mapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @GetMapping
    public List<MantenimientoDto> listarTodos() {
        return listar.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoDto> obtener(@PathVariable Integer id) {
        var encontrado = obtener.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }

    @GetMapping("/equipo/{serie}")
    public List<MantenimientoDto> listarEquipo(@PathVariable String serie) {
        return listarPorSerie.ejecutar(serie).stream().map(mapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoDto> actualizar(@PathVariable Integer id, @Valid @RequestBody MantenimientoDto dto) {
        var actualizado = actualizar.ejecutar(mapper.toDomain(new MantenimientoDto(
                id,
                dto.equipoId(),
                dto.serieSnapshot(),
                dto.idCliente(),
                dto.fechaProgramada(),
                dto.frecuenciaDias(),
                dto.descripcion(),
                dto.estado(),
                dto.creadoEn(),
                dto.estadoInterno()
        )));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminar.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<MantenimientoDto> iniciar(@PathVariable Integer id) {
        var actualizado = iniciar.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<MantenimientoDto> completar(@PathVariable Integer id) {
        var actualizado = completar.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }
}

