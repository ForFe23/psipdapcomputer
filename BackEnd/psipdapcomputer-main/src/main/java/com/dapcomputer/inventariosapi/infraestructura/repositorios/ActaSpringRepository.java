package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActaSpringRepository extends JpaRepository<ActaJpa, Integer> {
    List<ActaJpa> findByEstado(EstadoActa estado);

    List<ActaJpa> findByFechaActaBetween(LocalDate inicio, LocalDate fin);

    List<ActaJpa> findByIdCliente(Integer idCliente);

    List<ActaJpa> findByEntregadoPorIgnoreCaseOrRecibidoPorIgnoreCase(String entregadoPor, String recibidoPor);

    @Modifying
    @Query("update ActaJpa a set a.estadoInterno = :estado where a.idEquipo = :idEquipo")
    void actualizarEstadoInternoPorEquipo(@Param("idEquipo") Integer idEquipo, @Param("estado") String estado);

    @Modifying
    @Query("update ActaJpa a set a.estadoInterno = :estado where a.id = :id")
    void actualizarEstadoInterno(@Param("id") Integer id, @Param("estado") String estado);

    @Modifying
    @Query("update ActaJpa a set a.estadoInterno = :estado where a.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Integer idCliente, @Param("estado") String estado);

    java.util.Optional<ActaJpa> findFirstByIdEquipoOrderByIdDesc(Integer idEquipo);
}

