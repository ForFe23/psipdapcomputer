package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarUsuariosPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerUsuarioCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.UsuarioDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.UsuarioDtoMapper;
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
@RequestMapping("/api/usuarios")
public class UsuarioControlador {
    private final CrearUsuarioCasoUso crearUsuario;
    private final ListarUsuariosCasoUso listarUsuarios;
    private final ListarUsuariosPorClienteCasoUso listarPorCliente;
    private final ObtenerUsuarioCasoUso obtenerUsuario;
    private final EliminarUsuarioCasoUso eliminarUsuario;
    private final UsuarioDtoMapper mapper;

    public UsuarioControlador(CrearUsuarioCasoUso crearUsuario, ListarUsuariosCasoUso listarUsuarios, ListarUsuariosPorClienteCasoUso listarPorCliente, ObtenerUsuarioCasoUso obtenerUsuario, EliminarUsuarioCasoUso eliminarUsuario, UsuarioDtoMapper mapper) {
        this.crearUsuario = crearUsuario;
        this.listarUsuarios = listarUsuarios;
        this.listarPorCliente = listarPorCliente;
        this.obtenerUsuario = obtenerUsuario;
        this.eliminarUsuario = eliminarUsuario;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> crear(@Valid @RequestBody UsuarioDto solicitud) {
        var creado = crearUsuario.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioDto solicitud) {
        var entrada = new UsuarioDto(
                id,
                solicitud.idCliente(),
                solicitud.empresaId(),
                solicitud.cedula(),
                solicitud.apellidos(),
                solicitud.nombres(),
                solicitud.rolId(),
                solicitud.rolCodigo(),
                solicitud.solfrnrf(),
                solicitud.correo(),
                solicitud.telefono(),
                solicitud.estatus(),
                solicitud.fechaRegistro(),
                solicitud.estadoInterno());
        var actualizado = crearUsuario.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @GetMapping
    public List<UsuarioDto> listar() {
        return listarUsuarios.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<UsuarioDto> listarPorCliente(@PathVariable Long idCliente) {
        return listarPorCliente.ejecutar(idCliente).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtener(@PathVariable Integer id) {
        var usuario = obtenerUsuario.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminarUsuario.ejecutar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
