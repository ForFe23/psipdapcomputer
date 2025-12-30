package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActaSpringRepository extends JpaRepository<ActaJpa, Integer> {
}
