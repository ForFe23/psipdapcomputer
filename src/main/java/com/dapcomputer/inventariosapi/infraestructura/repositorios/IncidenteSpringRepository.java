package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.IncidenteJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenteSpringRepository extends JpaRepository<IncidenteJpa, Integer> {
    List<IncidenteJpa> findByIdCliente(Long idCliente);
    List<IncidenteJpa> findBySerieEquipo(String serieEquipo);
    List<IncidenteJpa> findByIdUsuario(Integer idUsuario);
}
