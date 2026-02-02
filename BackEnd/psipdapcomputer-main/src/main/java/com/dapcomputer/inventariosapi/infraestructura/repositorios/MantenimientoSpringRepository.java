package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MantenimientoJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MantenimientoSpringRepository extends JpaRepository<MantenimientoJpa, Integer> {
    List<MantenimientoJpa> findByEquipoId(Long equipoId);

    @Modifying
    @Query("update MantenimientoJpa m set m.estado = :estado, m.estadoInterno = :estado where m.equipoId = :equipoId")
    void actualizarEstadoPorEquipo(@Param("equipoId") Long equipoId, @Param("estado") String estado);

    @Modifying
    @Query("update MantenimientoJpa m set m.estado = :estado, m.estadoInterno = :estado where m.idCliente = :idCliente")
    void actualizarEstadoPorCliente(@Param("idCliente") Long idCliente, @Param("estado") String estado);
}
