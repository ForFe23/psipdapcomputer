package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ClienteJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteSpringRepository extends JpaRepository<ClienteJpa, Long> {
}
