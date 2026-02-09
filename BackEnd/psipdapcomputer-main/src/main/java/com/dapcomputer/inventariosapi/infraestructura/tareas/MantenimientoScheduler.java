package com.dapcomputer.inventariosapi.infraestructura.tareas;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MantenimientoScheduler {

    private final MantenimientoRepositorio repositorio;

    public MantenimientoScheduler(MantenimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Scheduled(cron = "0 0 8,14 * * *")
    public void marcarPendientes() {
        List<Mantenimiento> todos = repositorio.listar();
        LocalDateTime ahora = LocalDateTime.now();
        for (Mantenimiento m : todos) {
            if (m.estado() != null && (m.estado().equalsIgnoreCase("COMPLETADO") || m.estado().equalsIgnoreCase("EN_CURSO") || m.estado().equalsIgnoreCase("PENDIENTE"))) {
                continue;
            }
            if (m.fechaProgramada() != null && !m.fechaProgramada().isAfter(ahora)) {
                Mantenimiento actualizado = new Mantenimiento(
                        m.id(),
                        m.equipoId(),
                        m.serieSnapshot(),
                        m.idCliente(),
                        m.fechaProgramada(),
                        m.frecuenciaDias(),
                        m.descripcion(),
                        "PENDIENTE",
                        m.creadoEn(),
                        m.estadoInterno()
                );
                repositorio.guardar(actualizado);
            }
        }
    }
}

