package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActaSpringRepository extends JpaRepository<ActaJpa, Integer> {
    List<ActaJpa> findByEstado(EstadoActa estado);

    List<ActaJpa> findByFechaActaBetween(LocalDate inicio, LocalDate fin);

    List<ActaJpa> findByIdCliente(Integer idCliente);

    List<ActaJpa> findByEntregadoPorIgnoreCaseOrRecibidoPorIgnoreCase(String entregadoPor, String recibidoPor);
}
