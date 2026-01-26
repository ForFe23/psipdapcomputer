package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.IncidenteDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.IncidenteDtoMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidentes")
public class IncidenteControlador {
    private final RegistrarIncidenteCasoUso registrarIncidente;
    private final ListarIncidentesCasoUso listarIncidentes;
    private final ListarIncidentesPorClienteCasoUso listarPorCliente;
    private final ListarIncidentesPorEquipoCasoUso listarPorEquipo;
    private final ListarIncidentesPorUsuarioCasoUso listarPorUsuario;
    private final ObtenerIncidenteCasoUso obtenerIncidente;
    private final ActualizarIncidenteCasoUso actualizarIncidente;
    private final EliminarIncidenteCasoUso eliminarIncidente;
    private final IncidenteDtoMapper mapper;

    public IncidenteControlador(
            RegistrarIncidenteCasoUso registrarIncidente,
            ListarIncidentesCasoUso listarIncidentes,
            ListarIncidentesPorClienteCasoUso listarPorCliente,
            ListarIncidentesPorEquipoCasoUso listarPorEquipo,
            ListarIncidentesPorUsuarioCasoUso listarPorUsuario,
            ObtenerIncidenteCasoUso obtenerIncidente,
            ActualizarIncidenteCasoUso actualizarIncidente,
            EliminarIncidenteCasoUso eliminarIncidente,
            IncidenteDtoMapper mapper) {
        this.registrarIncidente = registrarIncidente;
        this.listarIncidentes = listarIncidentes;
        this.listarPorCliente = listarPorCliente;
        this.listarPorEquipo = listarPorEquipo;
        this.listarPorUsuario = listarPorUsuario;
        this.obtenerIncidente = obtenerIncidente;
        this.actualizarIncidente = actualizarIncidente;
        this.eliminarIncidente = eliminarIncidente;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<IncidenteDto> crear(@Valid @RequestBody IncidenteDto solicitud) {
        var creado = registrarIncidente.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @GetMapping
    public List<IncidenteDto> listarTodos() {
        return listarIncidentes.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenteDto> obtener(@PathVariable Integer id) {
        var encontrado = obtenerIncidente.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidenteDto> actualizar(@PathVariable Integer id, @Valid @RequestBody IncidenteDto solicitud) {
        var actualizado = actualizarIncidente.ejecutar(mapper.toDomain(new IncidenteDto(
                id,
                solicitud.equipoId(),
                solicitud.idUsuario(),
                solicitud.idCliente(),
                solicitud.fechaIncidente(),
                solicitud.detalle(),
                solicitud.costo(),
                solicitud.tecnico(),
                solicitud.responsable(),
                solicitud.estadoInterno()
        )));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminarIncidente.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<IncidenteDto> listarCliente(@PathVariable Long idCliente) {
        return listarPorCliente.ejecutar(idCliente).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/equipo/{equipoId}")
    public List<IncidenteDto> listarEquipo(@PathVariable Integer equipoId) {
        return listarPorEquipo.ejecutar(equipoId).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<IncidenteDto> listarUsuario(@PathVariable Integer idUsuario) {
        return listarPorUsuario.ejecutar(idUsuario).stream().map(mapper::toDto).toList();
    }
}
