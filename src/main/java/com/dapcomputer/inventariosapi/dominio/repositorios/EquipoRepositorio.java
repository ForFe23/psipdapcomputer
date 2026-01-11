package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import java.util.List;
import java.util.Optional;

public interface EquipoRepositorio {
    Equipo guardar(Equipo equipo);
    Optional<Equipo> buscarPorId(EquipoId id);
    List<Equipo> listar();
    Optional<Equipo> buscarPorSerie(String serie);
}
