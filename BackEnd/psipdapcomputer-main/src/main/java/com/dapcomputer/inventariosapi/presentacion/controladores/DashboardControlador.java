package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerDashboardResumenCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import com.dapcomputer.inventariosapi.presentacion.dto.DashboardResumenDto;
import com.dapcomputer.inventariosapi.presentacion.dto.KardexDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ListaResumenDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ResumenActaDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ResumenMovimientoDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardControlador {
    private final ObtenerDashboardResumenCasoUso obtenerDashboardResumenCasoUso;

    public DashboardControlador(ObtenerDashboardResumenCasoUso obtenerDashboardResumenCasoUso) {
        this.obtenerDashboardResumenCasoUso = obtenerDashboardResumenCasoUso;
    }

    @GetMapping("/resumen")
    public DashboardResumenDto resumen(@RequestParam(name = "kardexLimite", required = false) Integer kardexLimite) {
        int limite = kardexLimite != null && kardexLimite > 0 ? kardexLimite : 10;
        var resumen = obtenerDashboardResumenCasoUso.ejecutar(limite);

        List<ResumenActaDto> actas = resumen != null && resumen.actasAbiertas() != null
                ? resumen.actasAbiertas().stream()
                        .map(a -> new ResumenActaDto(a.id(), a.tema() != null ? a.tema() : a.equipoSerie(), a.estado(), a.fechaActa()))
                        .toList()
                : List.of();

        List<ResumenMovimientoDto> movimientos = resumen != null && resumen.movimientosAbiertos() != null
                ? resumen.movimientosAbiertos().stream()
                        .map(m -> new ResumenMovimientoDto(m.id(), m.serieEquipo(), m.estadoInterno(), m.fecha(), m.tipo()))
                        .toList()
                : List.of();

        List<KardexDto> kardex = resumen != null && resumen.kardexReciente() != null
                ? resumen.kardexReciente().stream().map(this::mapearKardex).toList()
                : List.of();

        return new DashboardResumenDto(
                new ListaResumenDto<>(actas.size(), actas),
                new ListaResumenDto<>(movimientos.size(), movimientos),
                kardex);
    }

    private KardexDto mapearKardex(KardexEntrada entrada) {
        if (entrada == null) {
            return new KardexDto(null, null, null, null, null, null, null, null, null, null, null, null);
        }
        return new KardexDto(
                entrada.movimientoId(),
                entrada.equipoId(),
                entrada.serieEquipo(),
                entrada.ubicacionOrigenId(),
                entrada.ubicacionOrigen(),
                entrada.ubicacionDestinoId(),
                entrada.ubicacionDestino(),
                entrada.ejecutadoPor(),
                entrada.fecha(),
                entrada.tipo(),
                entrada.accion(),
                entrada.observacion());
    }
}
