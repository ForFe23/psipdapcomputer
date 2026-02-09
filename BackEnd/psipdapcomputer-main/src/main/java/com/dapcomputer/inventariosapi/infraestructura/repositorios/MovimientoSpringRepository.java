package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimientoSpringRepository extends JpaRepository<MovimientoJpa, Integer> {
    List<MovimientoJpa> findByIdEquipo(Integer idEquipo);
    List<MovimientoJpa> findByIdUsuarioOrigenOrIdUsuarioDestino(Integer idUsuarioOrigen, Integer idUsuarioDestino);
    java.util.Optional<MovimientoJpa> findFirstByActa_IdOrderByIdDesc(Integer actaId);

    @Modifying
    @Query("update MovimientoJpa m set m.estadoInterno = :estado where m.idEquipo = :idEquipo")
    void actualizarEstadoInternoPorEquipo(@Param("idEquipo") Integer idEquipo, @Param("estado") String estado);

    @Modifying
    @Query("update MovimientoJpa m set m.estadoInterno = :estado where m.id = :id")
    void actualizarEstadoInterno(@Param("id") Integer id, @Param("estado") String estado);

    @Modifying
    @Query("update MovimientoJpa m set m.estadoInterno = :estado where m.acta.id = :idActa")
    void actualizarEstadoInternoPorActa(@Param("idActa") Integer idActa, @Param("estado") String estado);

    @Modifying
    @Query("update MovimientoJpa m set m.estadoInterno = :estado where m.acta.idCliente = :idCliente or m.equipo.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Long idCliente, @Param("estado") String estado);
}

