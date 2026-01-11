package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarMovimientoCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.MovimientoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.MovimientoDtoMapper;
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
@RequestMapping("/api/movimientos")
public class MovimientoControlador {
    private final RegistrarMovimientoCasoUso registrarMovimiento;
    private final ListarMovimientosPorEquipoCasoUso listarPorEquipo;
    private final ListarMovimientosPorUsuarioCasoUso listarPorUsuario;
    private final MovimientoDtoMapper mapper = new MovimientoDtoMapper();

    public MovimientoControlador(RegistrarMovimientoCasoUso registrarMovimiento, ListarMovimientosPorEquipoCasoUso listarPorEquipo, ListarMovimientosPorUsuarioCasoUso listarPorUsuario) {
        this.registrarMovimiento = registrarMovimiento;
        this.listarPorEquipo = listarPorEquipo;
        this.listarPorUsuario = listarPorUsuario;
    }

    @PostMapping
    public ResponseEntity<MovimientoDto> crear(@Valid @RequestBody MovimientoDto solicitud) {
        var creado = registrarMovimiento.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.ok(mapper.toDto(creado));
    }

    @GetMapping("/equipo/{serie}")
    public List<MovimientoDto> listarPorEquipo(@PathVariable String serie) {
        return mapper.toDtoList(listarPorEquipo.ejecutar(serie));
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<MovimientoDto> listarPorUsuario(@PathVariable Integer idUsuario) {
        return mapper.toDtoList(listarPorUsuario.ejecutar(idUsuario));
    }
}
