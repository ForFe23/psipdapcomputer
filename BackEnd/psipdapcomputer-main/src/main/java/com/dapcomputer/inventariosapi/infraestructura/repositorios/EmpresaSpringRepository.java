package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EmpresaJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpresaSpringRepository extends JpaRepository<EmpresaJpa, Long> {
    List<EmpresaJpa> findByClienteId(Long clienteId);

    @Modifying
    @Query("update EmpresaJpa e set e.estadoInterno = :estado where e.id = :id")
    void actualizarEstadoInterno(@Param("id") Long id, @Param("estado") String estado);

    @Modifying
    @Query("update EmpresaJpa e set e.estadoInterno = :estado where e.clienteId = :clienteId")
    void actualizarEstadoInternoPorCliente(@Param("clienteId") Long clienteId, @Param("estado") String estado);
}
