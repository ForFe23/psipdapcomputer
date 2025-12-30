package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerifericoSpringRepository extends JpaRepository<PerifericoJpa, PerifericoJpaId> {
    List<PerifericoJpa> findByIdIdAndIdSerieEquipo(Integer id, String serieEquipo);
}
