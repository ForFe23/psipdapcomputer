package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.repositorios.IPersonaRepositorio;
import com.dapcomputer.inventariosapi.presentacion.dto.PersonaDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.PersonaDtoMapper;
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
@RequestMapping("/api/personas")
public class PersonaControlador {
    private final IPersonaRepositorio repositorio;
    private final PersonaDtoMapper mapper;

    public PersonaControlador(IPersonaRepositorio repositorio, PersonaDtoMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @GetMapping
    public List<PersonaDto> listar(@RequestParam(value = "empresaId", required = false) Long empresaId,
                                   @RequestParam(value = "clienteId", required = false) Long clienteId) {
        var data = empresaId != null
                ? repositorio.listarPorEmpresa(empresaId)
                : clienteId != null
                    ? repositorio.listarPorCliente(clienteId)
                    : repositorio.listar();
        return data.stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDto> obtener(@PathVariable Integer id) {
        return repositorio.buscarPorId(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PersonaDto> crear(@Valid @RequestBody PersonaDto solicitud) {
        validarClienteOEmpresa(solicitud);
        var guardado = repositorio.guardar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDto> actualizar(@PathVariable Integer id, @Valid @RequestBody PersonaDto solicitud) {
        validarClienteOEmpresa(solicitud);
        var entrada = new PersonaDto(id, solicitud.clienteId(), solicitud.empresaId(), solicitud.cedula(), solicitud.apellidos(), solicitud.nombres(), solicitud.correo(), solicitud.telefono(), solicitud.cargo(), solicitud.estadoInterno());
        var guardado = repositorio.actualizar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        repositorio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarClienteOEmpresa(PersonaDto dto) {
        if (dto.clienteId() == null && dto.empresaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar clienteId o empresaId.");
        }
    }
}

