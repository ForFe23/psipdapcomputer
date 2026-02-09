package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaAdjuntoJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActaAdjuntoSpringRepository extends JpaRepository<ActaAdjuntoJpa, Integer> {
    List<ActaAdjuntoJpa> findByActa_Id(Integer actaId);

    @Modifying
    @Query("update ActaAdjuntoJpa a set a.estadoInterno = :estado where a.id = :id")
    void actualizarEstadoInterno(@Param("id") Integer id, @Param("estado") String estado);

    @Modifying
    @Query("update ActaAdjuntoJpa a set a.estadoInterno = :estado where a.acta.id = :idActa")
    void actualizarEstadoInternoPorActa(@Param("idActa") Integer idActa, @Param("estado") String estado);

    @Modifying
    @Query("update ActaAdjuntoJpa a set a.estadoInterno = :estado where a.acta.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Integer idCliente, @Param("estado") String estado);

    @Modifying
    @Query("update ActaAdjuntoJpa a set a.estadoInterno = :estado where a.acta.idEquipo = :idEquipo")
    void actualizarEstadoInternoPorEquipo(@Param("idEquipo") Integer idEquipo, @Param("estado") String estado);
}

