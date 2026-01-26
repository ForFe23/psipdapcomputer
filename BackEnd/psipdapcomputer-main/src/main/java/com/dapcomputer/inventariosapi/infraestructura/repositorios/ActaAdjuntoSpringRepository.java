package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaAdjuntoJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActaAdjuntoSpringRepository extends JpaRepository<ActaAdjuntoJpa, Integer> {
    List<ActaAdjuntoJpa> findByActa_Id(Integer actaId);
}
