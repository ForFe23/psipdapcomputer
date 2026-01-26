package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EmpresaJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaSpringRepository extends JpaRepository<EmpresaJpa, Long> {
}
