package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.repositorios.RolRepositorio;
import com.dapcomputer.inventariosapi.presentacion.dto.RolDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.RolDtoMapper;
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
@RequestMapping("/api/roles")
public class RolControlador {
    private final RolRepositorio repositorio;
    private final RolDtoMapper mapper;

    public RolControlador(RolRepositorio repositorio, RolDtoMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @GetMapping
    public List<RolDto> listar() {
        return repositorio.listar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDto> obtener(@PathVariable Long id) {
        return repositorio.buscarPorId(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RolDto> crear(@Valid @RequestBody RolDto solicitud) {
        var guardado = repositorio.guardar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDto> actualizar(@PathVariable Long id, @Valid @RequestBody RolDto solicitud) {
        var entrada = new RolDto(id, solicitud.codigo(), solicitud.nombre());
        var guardado = repositorio.guardar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repositorio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
