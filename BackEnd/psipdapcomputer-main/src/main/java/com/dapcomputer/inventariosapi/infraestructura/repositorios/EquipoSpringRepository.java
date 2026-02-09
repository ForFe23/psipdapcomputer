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

    List<EquipoJpa> findByIdCliente(Long idCliente);
    List<EquipoJpa> findByEmpresaId(Long empresaId);

    @Query("select coalesce(max(e.id),0) + 1 from EquipoJpa e")
    Integer siguienteId();

    @Modifying
    @Query("update EquipoJpa e set e.estadoInterno = :estado where e.id = :id")
    void actualizarEstadoInterno(@Param("id") Integer id, @Param("estado") String estado);

    @Modifying
    @Query("update EquipoJpa e set e.estadoInterno = :estado where e.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Long idCliente, @Param("estado") String estado);

    @Modifying
    @Query("update EquipoJpa e set e.estadoInterno = :estado where e.empresaId = :empresaId")
    void actualizarEstadoInternoPorEmpresa(@Param("empresaId") Long empresaId, @Param("estado") String estado);

    @Modifying
    @Query("update EquipoJpa e set e.ubicacionActualId = :ubicacionId, e.ubicacionUsuario = :ubicacionTexto where e.id = :id")
    void actualizarUbicacion(@Param("id") Integer id, @Param("ubicacionId") Long ubicacionId, @Param("ubicacionTexto") String ubicacionTexto);

    @Modifying
    @Query("update EquipoJpa e set e.ubicacionActualId = :ubicacionId, e.ubicacionUsuario = :ubicacionTexto, e.empresaId = :empresaId where e.id = :id")
    void actualizarUbicacionYEmpresa(@Param("id") Integer id, @Param("ubicacionId") Long ubicacionId, @Param("ubicacionTexto") String ubicacionTexto, @Param("empresaId") Long empresaId);
}

