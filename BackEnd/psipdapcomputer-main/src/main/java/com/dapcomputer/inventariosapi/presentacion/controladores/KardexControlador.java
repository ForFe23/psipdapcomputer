package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarIncidentesPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarMovimientosPorEquipoCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.IncidenteDto;
import com.dapcomputer.inventariosapi.presentacion.dto.MovimientoDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.IncidenteDtoMapper;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.MovimientoDtoMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kardex")
public class KardexControlador {
    private final ListarMovimientosPorEquipoCasoUso listarMovimientos;
    private final ListarIncidentesPorEquipoCasoUso listarIncidentes;
    private final MovimientoDtoMapper movimientoMapper;
    private final IncidenteDtoMapper incidenteMapper;

    public KardexControlador(
            ListarMovimientosPorEquipoCasoUso listarMovimientos,
            ListarIncidentesPorEquipoCasoUso listarIncidentes,
            MovimientoDtoMapper movimientoMapper,
            IncidenteDtoMapper incidenteMapper) {
        this.listarMovimientos = listarMovimientos;
        this.listarIncidentes = listarIncidentes;
        this.movimientoMapper = movimientoMapper;
        this.incidenteMapper = incidenteMapper;
    }

    @GetMapping("/equipo/{equipoId}")
    public Map<String, Object> kardexEquipo(@PathVariable Integer equipoId) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("movimientos", listarMovimientos.ejecutar(equipoId).stream().map(movimientoMapper::toDto).toList());
        respuesta.put("incidentes", listarIncidentes.ejecutar(equipoId).stream().map(incidenteMapper::toDto).toList());
        return respuesta;
    }
}
