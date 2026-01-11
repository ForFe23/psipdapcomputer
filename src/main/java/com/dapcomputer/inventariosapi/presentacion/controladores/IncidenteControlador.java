package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarIncidenteCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.IncidenteDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.IncidenteDtoMapper;
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
@RequestMapping("/api/incidentes")
public class IncidenteControlador {
    private final RegistrarIncidenteCasoUso registrarIncidente;
    private final ListarIncidentesCasoUso listarIncidentes;
    private final ListarIncidentesPorClienteCasoUso listarPorCliente;
    private final ListarIncidentesPorEquipoCasoUso listarPorEquipo;
    private final ListarIncidentesPorUsuarioCasoUso listarPorUsuario;
    private final IncidenteDtoMapper mapper;

    public IncidenteControlador(
            RegistrarIncidenteCasoUso registrarIncidente,
            ListarIncidentesCasoUso listarIncidentes,
            ListarIncidentesPorClienteCasoUso listarPorCliente,
            ListarIncidentesPorEquipoCasoUso listarPorEquipo,
            ListarIncidentesPorUsuarioCasoUso listarPorUsuario,
            IncidenteDtoMapper mapper) {
        this.registrarIncidente = registrarIncidente;
        this.listarIncidentes = listarIncidentes;
        this.listarPorCliente = listarPorCliente;
        this.listarPorEquipo = listarPorEquipo;
        this.listarPorUsuario = listarPorUsuario;
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

    @GetMapping("/cliente/{idCliente}")
    public List<IncidenteDto> listarCliente(@PathVariable Long idCliente) {
        return listarPorCliente.ejecutar(idCliente).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/equipo/{serie}")
    public List<IncidenteDto> listarEquipo(@PathVariable String serie) {
        return listarPorEquipo.ejecutar(serie).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<IncidenteDto> listarUsuario(@PathVariable Integer idUsuario) {
        return listarPorUsuario.ejecutar(idUsuario).stream().map(mapper::toDto).toList();
    }
}
