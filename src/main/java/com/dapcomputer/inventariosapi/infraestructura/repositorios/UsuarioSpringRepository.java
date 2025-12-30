package com.dapcomputer.inventariosapi.infraestructura.repositorios;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioSpringRepository extends JpaRepository<UsuarioJpa, Integer> {
    List<UsuarioJpa> findByIdCliente(Long idCliente);
}
