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
    private final MovimientoDtoMapper movimientoMapper = new MovimientoDtoMapper();
    private final IncidenteDtoMapper incidenteMapper = new IncidenteDtoMapper();

    public KardexControlador(
            ListarMovimientosPorEquipoCasoUso listarMovimientos,
            ListarIncidentesPorEquipoCasoUso listarIncidentes) {
        this.listarMovimientos = listarMovimientos;
        this.listarIncidentes = listarIncidentes;
    }

    @GetMapping("/equipo/{serie}")
    public Map<String, Object> kardexEquipo(@PathVariable String serie) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("movimientos", movimientoMapper.toDtoList(listarMovimientos.ejecutar(serie)));
        respuesta.put("incidentes", incidenteMapper.toDtoList(listarIncidentes.ejecutar(serie)));
        return respuesta;
    }
}
