package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerifericoSpringRepository extends JpaRepository<PerifericoJpa, Integer> {
    List<PerifericoJpa> findByEquipoId(Integer equipoId);
    List<PerifericoJpa> findBySerieEquipo(String serieEquipo);
    List<PerifericoJpa> findByIdCliente(Integer idCliente);

    @Modifying
    @Query("update PerifericoJpa p set p.estadoInterno = :estado where p.equipoId = :equipoId")
    void actualizarEstadoInternoPorEquipo(@Param("equipoId") Integer equipoId, @Param("estado") String estado);

    @Modifying
    @Query("update PerifericoJpa p set p.estadoInterno = :estado where p.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Integer idCliente, @Param("estado") String estado);
}
