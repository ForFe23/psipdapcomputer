package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UbicacionJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionSpringRepository extends JpaRepository<UbicacionJpa, Long> {
    List<UbicacionJpa> findByEmpresaId(Long empresaId);
}
