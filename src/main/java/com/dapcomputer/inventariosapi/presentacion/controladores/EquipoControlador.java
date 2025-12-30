package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarEquiposCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerEquipoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.presentacion.dto.EquipoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.EquipoDtoMapper;
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
@RequestMapping("/api/equipos")
public class EquipoControlador {
    private final CrearEquipoCasoUso crearEquipo;
    private final ListarEquiposCasoUso listarEquipos;
    private final ObtenerEquipoCasoUso obtenerEquipo;
    private final EquipoDtoMapper mapper = new EquipoDtoMapper();

    public EquipoControlador(CrearEquipoCasoUso crearEquipo, ListarEquiposCasoUso listarEquipos, ObtenerEquipoCasoUso obtenerEquipo) {
        this.crearEquipo = crearEquipo;
        this.listarEquipos = listarEquipos;
        this.obtenerEquipo = obtenerEquipo;
    }

    @PostMapping
    public ResponseEntity<EquipoDto> crear(@Valid @RequestBody EquipoDto solicitud) {
        var creado = crearEquipo.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.ok(mapper.toDto(creado));
    }

    @GetMapping
    public List<EquipoDto> listar() {
        return listarEquipos.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}/{serie}")
    public ResponseEntity<EquipoDto> obtener(@PathVariable Integer id, @PathVariable String serie) {
        var encontrado = obtenerEquipo.ejecutar(new EquipoId(id, serie));
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }
}
