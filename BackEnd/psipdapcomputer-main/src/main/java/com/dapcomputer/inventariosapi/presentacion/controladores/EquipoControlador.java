package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.BajaLogicaEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposPorUbicacionCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException;
import com.dapcomputer.inventariosapi.presentacion.dto.EquipoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.EquipoDtoMapper;
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
@RequestMapping("/api/equipos")
public class EquipoControlador {
    private final CrearEquipoCasoUso crearEquipo;
    private final ListarEquiposCasoUso listarEquipos;
    private final ObtenerEquipoCasoUso obtenerEquipo;
    private final BajaLogicaEquipoCasoUso bajaLogicaEquipo;
    private final ListarEquiposPorEstadoCasoUso listarPorEstado;
    private final ListarEquiposPorUbicacionCasoUso listarPorUbicacion;
    private final EquipoDtoMapper mapper;

    public EquipoControlador(CrearEquipoCasoUso crearEquipo, ListarEquiposCasoUso listarEquipos, ListarEquiposPorEstadoCasoUso listarPorEstado, ListarEquiposPorUbicacionCasoUso listarPorUbicacion, ObtenerEquipoCasoUso obtenerEquipo, BajaLogicaEquipoCasoUso bajaLogicaEquipo, EquipoDtoMapper mapper) {
        this.crearEquipo = crearEquipo;
        this.listarEquipos = listarEquipos;
        this.listarPorEstado = listarPorEstado;
        this.listarPorUbicacion = listarPorUbicacion;
        this.obtenerEquipo = obtenerEquipo;
        this.bajaLogicaEquipo = bajaLogicaEquipo;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<EquipoDto> crear(@Valid @RequestBody EquipoDto solicitud) {
        var creado = crearEquipo.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoDto> actualizarPorId(@PathVariable Integer id, @Valid @RequestBody EquipoDto solicitud) {
        var entrada = new EquipoDto(
                id,
                solicitud.serie(),
                solicitud.idCliente(),
                solicitud.empresaId(),
                solicitud.ubicacionActualId(),
                solicitud.asignadoAId(),
                solicitud.marca(),
                solicitud.modelo(),
                solicitud.tipo(),
                solicitud.sistemaOperativo(),
                solicitud.procesador(),
                solicitud.memoria(),
                solicitud.disco(),
                solicitud.fechaCompra(),
                solicitud.estado(),
                solicitud.estadoInterno(),
                solicitud.ip(),
                solicitud.ubicacionUsuario(),
                solicitud.departamentoUsuario(),
                solicitud.nombreUsuario(),
                solicitud.nombreProveedor(),
                solicitud.direccionProveedor(),
                solicitud.telefonoProveedor(),
                solicitud.contactoProveedor(),
                solicitud.cliente(),
                solicitud.activo(),
                solicitud.office(),
                solicitud.costo(),
                solicitud.factura(),
                solicitud.notas(),
                solicitud.ciudad(),
                solicitud.nombre(),
                solicitud.alias());
        var actualizado = crearEquipo.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @GetMapping
    public List<EquipoDto> listar() {
        return listarEquipos.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/estado")
    public List<EquipoDto> listarPorEstado(@RequestParam String estado) {
        return listarPorEstado.ejecutar(estado).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/ubicacion")
    public List<EquipoDto> listarPorUbicacion(@RequestParam String ubicacion) {
        return listarPorUbicacion.ejecutar(ubicacion).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/serie/{serie}")
    public ResponseEntity<EquipoDto> obtenerPorSerie(@PathVariable String serie) {
        var encontrado = listarEquipos.ejecutar().stream()
                .filter(e -> serie.equalsIgnoreCase(e.serie()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> obtenerPorId(@PathVariable Integer id) {
        var encontrado = listarEquipos.ejecutar().stream()
                .filter(e -> id.equals(e.id()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bajaLogicaEquipo.ejecutar(id);
        return ResponseEntity.noContent().build();
    }
}
