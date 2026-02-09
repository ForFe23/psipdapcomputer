package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PersonaJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonaSpringRepository extends JpaRepository<PersonaJpa, Integer> {
    List<PersonaJpa> findByEmpresaId(Long empresaId);
    List<PersonaJpa> findByClienteId(Long clienteId);

    @Modifying
    @Query("update PersonaJpa p set p.estadoInterno = :estado where p.empresaId = :empresaId")
    void actualizarEstadoInternoPorEmpresa(@Param("empresaId") Long empresaId, @Param("estado") String estado);

    @Modifying
    @Query("update PersonaJpa p set p.estadoInterno = :estado where p.clienteId = :clienteId")
    void actualizarEstadoInternoPorCliente(@Param("clienteId") Long clienteId, @Param("estado") String estado);
}

