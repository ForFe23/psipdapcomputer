package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.RolJpa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolSpringRepository extends JpaRepository<RolJpa, Long> {
    Optional<RolJpa> findByCodigo(String codigo);
}

