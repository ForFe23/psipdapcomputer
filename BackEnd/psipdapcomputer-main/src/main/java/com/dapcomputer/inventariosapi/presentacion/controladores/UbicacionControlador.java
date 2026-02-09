package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.repositorios.IUbicacionRepositorio;
import com.dapcomputer.inventariosapi.presentacion.dto.UbicacionDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.UbicacionDtoMapper;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionControlador {
    private final IUbicacionRepositorio repositorio;
    private final UbicacionDtoMapper mapper;

    public UbicacionControlador(IUbicacionRepositorio repositorio, UbicacionDtoMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @GetMapping
    public List<UbicacionDto> listar(@RequestParam(value = "empresaId", required = false) Long empresaId,
                                     @RequestParam(value = "clienteId", required = false) Long clienteId) {
        var data = empresaId != null
                ? repositorio.listarPorEmpresa(empresaId)
                : clienteId != null
                    ? repositorio.listarPorCliente(clienteId)
                    : repositorio.listar();
        return data.stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDto> obtener(@PathVariable Long id) {
        return repositorio.buscarPorId(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UbicacionDto> crear(@Valid @RequestBody UbicacionDto solicitud) {
        validarAlMenosClienteOEempresa(solicitud);
        var guardado = repositorio.guardar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDto> actualizar(@PathVariable Long id, @Valid @RequestBody UbicacionDto solicitud) {
        validarAlMenosClienteOEempresa(solicitud);
        var entrada = new UbicacionDto(id, solicitud.clienteId(), solicitud.empresaId(), solicitud.nombre(), solicitud.direccion(), solicitud.estado(), solicitud.estadoInterno());
        var guardado = repositorio.actualizar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repositorio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarAlMenosClienteOEempresa(UbicacionDto dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ubicacion vacía");
        }
        if (dto.clienteId() == null && dto.empresaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar clienteId o empresaId para la ubicación.");
        }
    }
}

