package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioSpringRepository extends JpaRepository<UsuarioJpa, Integer> {
    List<UsuarioJpa> findByIdCliente(Long idCliente);

    @Modifying
    @Query("update UsuarioJpa u set u.estadoInterno = :estadoInterno, u.estatus = :estatus where u.id = :id")
    void actualizarEstadoInterno(@Param("id") Integer id, @Param("estadoInterno") String estadoInterno, @Param("estatus") String estatus);

    @Modifying
    @Query("update UsuarioJpa u set u.estadoInterno = :estadoInterno, u.estatus = :estatus where u.idCliente = :idCliente")
    void actualizarEstadoInternoPorCliente(@Param("idCliente") Long idCliente, @Param("estadoInterno") String estadoInterno, @Param("estatus") String estatus);
}
