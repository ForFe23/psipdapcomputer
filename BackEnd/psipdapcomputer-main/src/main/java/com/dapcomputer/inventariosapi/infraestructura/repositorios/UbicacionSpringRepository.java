package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UbicacionJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UbicacionSpringRepository extends JpaRepository<UbicacionJpa, Long> {
    List<UbicacionJpa> findByEmpresaId(Long empresaId);

    @Modifying
    @Query("update UbicacionJpa u set u.estadoInterno = :estado where u.empresaId = :empresaId")
    void actualizarEstadoInternoPorEmpresa(@Param("empresaId") Long empresaId, @Param("estado") String estado);
}
