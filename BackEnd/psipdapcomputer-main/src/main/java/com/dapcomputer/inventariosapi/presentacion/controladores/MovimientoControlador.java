package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.MovimientoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.MovimientoDtoMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoControlador {
    private final RegistrarMovimientoCasoUso registrarMovimiento;
    private final ListarMovimientosCasoUso listarMovimientos;
    private final ListarMovimientosPorEquipoCasoUso listarPorEquipo;
    private final ListarMovimientosPorUsuarioCasoUso listarPorUsuario;
    private final MovimientoDtoMapper mapper;

    public MovimientoControlador(RegistrarMovimientoCasoUso registrarMovimiento, ListarMovimientosCasoUso listarMovimientos, ListarMovimientosPorEquipoCasoUso listarPorEquipo, ListarMovimientosPorUsuarioCasoUso listarPorUsuario, MovimientoDtoMapper mapper) {
        this.registrarMovimiento = registrarMovimiento;
        this.listarMovimientos = listarMovimientos;
        this.listarPorEquipo = listarPorEquipo;
        this.listarPorUsuario = listarPorUsuario;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<MovimientoDto> crear(@Valid @RequestBody MovimientoDto solicitud) {
        var creado = registrarMovimiento.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @GetMapping
    public List<MovimientoDto> listar() {
        return listarMovimientos.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDto> obtener(@PathVariable Integer id) {
        var encontrado = listarMovimientos.ejecutar().stream()
                .filter(m -> id.equals(m.id()))
                .findFirst()
                .orElseThrow(() -> new com.dapcomputer.inventariosapi.aplicacion.excepciones.RecursoNoEncontradoException("Movimiento no encontrado"));
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }

    @GetMapping("/equipo/{equipoId}")
    public List<MovimientoDto> listarPorEquipo(@PathVariable Integer equipoId) {
        return listarPorEquipo.ejecutar(equipoId).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<MovimientoDto> listarPorUsuario(@PathVariable Integer idUsuario) {
        return listarPorUsuario.ejecutar(idUsuario).stream().map(mapper::toDto).toList();
    }
}
