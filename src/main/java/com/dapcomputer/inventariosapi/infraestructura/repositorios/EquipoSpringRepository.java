package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoSpringRepository extends JpaRepository<EquipoJpa, EquipoJpaId> {
}
