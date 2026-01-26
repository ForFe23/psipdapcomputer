package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.IncidenteJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncidenteSpringRepository extends JpaRepository<IncidenteJpa, Integer> {
    List<IncidenteJpa> findByIdCliente(Long idCliente);
    List<IncidenteJpa> findByEquipoId(Integer equipoId);
    List<IncidenteJpa> findByIdUsuario(Integer idUsuario);

    @Modifying
    @Query("update IncidenteJpa i set i.estadoInterno = :estado where i.equipoId = :equipoId")
    void actualizarEstadoInternoPorEquipo(@Param("equipoId") Integer equipoId, @Param("estado") String estado);
}
