package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ClienteJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteSpringRepository extends JpaRepository<ClienteJpa, Long> {
    @Modifying
    @Query("update ClienteJpa c set c.estadoInterno = :estadoInterno where c.id = :id")
    void actualizarEstadoInterno(@Param("id") Long id, @Param("estadoInterno") String estadoInterno);
}

