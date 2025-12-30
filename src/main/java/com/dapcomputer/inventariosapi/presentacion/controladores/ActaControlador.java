package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.ActaDtoMapper;
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
@RequestMapping("/api/actas")
public class ActaControlador {
    private final RegistrarActaCasoUso registrarActa;
    private final ListarActasCasoUso listarActas;
    private final ObtenerActaCasoUso obtenerActa;
    private final ActaDtoMapper mapper = new ActaDtoMapper();

    public ActaControlador(RegistrarActaCasoUso registrarActa, ListarActasCasoUso listarActas, ObtenerActaCasoUso obtenerActa) {
        this.registrarActa = registrarActa;
        this.listarActas = listarActas;
        this.obtenerActa = obtenerActa;
    }

    @PostMapping
    public ResponseEntity<ActaDto> crear(@Valid @RequestBody ActaDto solicitud) {
        var creada = registrarActa.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.ok(mapper.toDto(creada));
    }

    @GetMapping
    public List<ActaDto> listar() {
        return listarActas.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActaDto> obtener(@PathVariable Integer id) {
        var encontrada = obtenerActa.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(encontrada));
    }
}
