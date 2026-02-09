package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActaSecuenciaSpringRepository extends JpaRepository<ActaSecuenciaJpa, ActaSecuenciaJpaId> {
}

