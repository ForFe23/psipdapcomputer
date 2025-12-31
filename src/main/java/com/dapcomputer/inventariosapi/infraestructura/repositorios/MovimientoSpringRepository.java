package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoSpringRepository extends JpaRepository<MovimientoJpa, Integer> {
    List<MovimientoJpa> findBySerieEquipo(String serieEquipo);
    List<MovimientoJpa> findByIdUsuarioOrigenOrIdUsuarioDestino(Integer idUsuarioOrigen, Integer idUsuarioDestino);
}
