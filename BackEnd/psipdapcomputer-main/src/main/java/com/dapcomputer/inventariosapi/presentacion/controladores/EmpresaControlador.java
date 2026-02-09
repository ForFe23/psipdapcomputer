package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.repositorios.IEmpresaRepositorio;
import com.dapcomputer.inventariosapi.presentacion.dto.EmpresaDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.EmpresaDtoMapper;
import com.dapcomputer.inventariosapi.seguridad.SecurityUtils;
import com.dapcomputer.inventariosapi.seguridad.TokenPayload;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/api/empresas")
public class EmpresaControlador {
    private final IEmpresaRepositorio repositorio;
    private final EmpresaDtoMapper mapper;

    public EmpresaControlador(IEmpresaRepositorio repositorio, EmpresaDtoMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EmpresaDto> listar(@RequestParam(value = "clienteId", required = false) Long clienteId) {
        Optional<TokenPayload> payload = SecurityUtils.getPayload();
        String rol = payload.map(TokenPayload::rol).orElse("");
        Long scopedCliente = payload.map(TokenPayload::clienteId).orElse(null);

        Long objetivo = clienteId;
        boolean rolGlobal = "ADMIN_GLOBAL".equalsIgnoreCase(rol) || "TECNICO_GLOBAL".equalsIgnoreCase(rol);
        if (!rolGlobal) {
            if (scopedCliente != null) {
                objetivo = scopedCliente;
            } else if (clienteId == null) {
                objetivo = -1L;
            }
        }

        var lista = objetivo != null ? repositorio.listarPorCliente(objetivo) : repositorio.listar();
        return lista.stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDto> obtener(@PathVariable Long id) {
        return repositorio.buscarPorId(id).map(mapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaDto> crear(@Valid @RequestBody EmpresaDto solicitud) {
        var guardada = repositorio.guardar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDto> actualizar(@PathVariable Long id, @Valid @RequestBody EmpresaDto solicitud) {
        var entrada = new EmpresaDto(id, solicitud.clienteId(), solicitud.nombre(), solicitud.estado(), solicitud.estadoInterno());
        var guardada = repositorio.actualizar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(guardada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repositorio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

