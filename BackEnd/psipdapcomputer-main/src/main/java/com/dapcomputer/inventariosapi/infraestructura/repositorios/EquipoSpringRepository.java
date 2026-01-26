package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipoSpringRepository extends JpaRepository<EquipoJpa, Integer> {
    Optional<EquipoJpa> findBySerieEquipo(String serieEquipo);

    List<EquipoJpa> findByEstadoIgnoreCase(String estado);

    List<EquipoJpa> findByUbicacionUsuarioIgnoreCase(String ubicacionUsuario);

    @Query("select coalesce(max(e.id),0) + 1 from EquipoJpa e")
    Integer siguienteId();

    @Modifying
    @Query("update EquipoJpa e set e.estadoInterno = :estado where e.id = :id")
    void actualizarEstadoInterno(@Param("id") Integer id, @Param("estado") String estado);
}
