package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpaId;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoSpringRepository extends JpaRepository<EquipoJpa, EquipoJpaId> {
    Optional<EquipoJpa> findBySerieEquipo(String serieEquipo);

    List<EquipoJpa> findByEstadoIgnoreCase(String estado);

    List<EquipoJpa> findByUbicacionUsuarioIgnoreCase(String ubicacionUsuario);
}
