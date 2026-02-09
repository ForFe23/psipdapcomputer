package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.Equipo;
import java.util.List;
import java.util.Optional;

public interface IEquipoRepositorio {
    Equipo guardar(Equipo equipo);
    Equipo actualizar(Equipo equipo);
    Optional<Equipo> buscarPorId(Integer id);
    Optional<Equipo> buscarPorSerie(String serie);
    List<Equipo> buscarPorFiltros(Long empresaId, Long ubicacionId, Integer asignadoAId, String serie);
}

