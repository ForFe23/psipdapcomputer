package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorRangoFechaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.ActaDtoMapper;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final ListarActasPorEstadoCasoUso listarPorEstado;
    private final ListarActasPorRangoFechaCasoUso listarPorRango;
    private final ListarActasPorClienteCasoUso listarPorCliente;
    private final ListarActasPorUsuarioCasoUso listarPorUsuario;
    private final ActaDtoMapper mapper;

    public ActaControlador(
            RegistrarActaCasoUso registrarActa,
            ListarActasCasoUso listarActas,
            ObtenerActaCasoUso obtenerActa,
            ListarActasPorEstadoCasoUso listarPorEstado,
            ListarActasPorRangoFechaCasoUso listarPorRango,
            ListarActasPorClienteCasoUso listarPorCliente,
            ListarActasPorUsuarioCasoUso listarPorUsuario,
            ActaDtoMapper mapper) {
        this.registrarActa = registrarActa;
        this.listarActas = listarActas;
        this.obtenerActa = obtenerActa;
        this.listarPorEstado = listarPorEstado;
        this.listarPorRango = listarPorRango;
        this.listarPorCliente = listarPorCliente;
        this.listarPorUsuario = listarPorUsuario;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ActaDto> crear(@Valid @RequestBody ActaDto solicitud) {
        var creada = registrarActa.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creada));
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

    @GetMapping("/{id}/items")
    public List<ActaItemDto> obtenerItems(@PathVariable Integer id) {
        var acta = obtenerActa.ejecutar(id);
        return mapper.toDtoItems(acta);
    }

    @GetMapping("/estado")
    public List<ActaDto> listarPorEstado(@RequestParam EstadoActa estado) {
        return listarPorEstado.ejecutar(estado).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/rango-fecha")
    public List<ActaDto> listarPorRango(
            @RequestParam("inicio") LocalDate inicio,
            @RequestParam("fin") LocalDate fin) {
        return listarPorRango.ejecutar(inicio, fin).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<ActaDto> listarPorCliente(@PathVariable Integer idCliente) {
        return listarPorCliente.ejecutar(idCliente).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/usuario")
    public List<ActaDto> listarPorUsuario(@RequestParam String nombre) {
        return listarPorUsuario.ejecutar(nombre).stream().map(mapper::toDto).toList();
    }
}
