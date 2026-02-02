package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaItemJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActaItemSpringRepository extends JpaRepository<ActaItemJpa, Integer> {
    List<ActaItemJpa> findByActa_Id(Integer actaId);

    @Modifying
    @Query("update ActaItemJpa ai set ai.estadoInterno = :estado where ai.acta.id = :idActa")
    void actualizarEstadoInternoPorActa(@Param("idActa") Integer idActa, @Param("estado") String estado);

    @Modifying
    @Query("update ActaItemJpa ai set ai.estadoInterno = :estado where ai.equipoId = :idEquipo")
    void actualizarEstadoInternoPorEquipo(@Param("idEquipo") Integer idEquipo, @Param("estado") String estado);

    @Modifying
    @Query("update ActaItemJpa ai set ai.estadoInterno = :estado where ai.acta.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Integer idCliente, @Param("estado") String estado);
}
