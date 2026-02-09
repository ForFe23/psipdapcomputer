package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import java.util.List;
import java.util.Optional;

public interface EquipoRepositorio {
    Equipo guardar(Equipo equipo);
    Optional<Equipo> buscarPorId(Integer id);
    List<Equipo> listar();
    Optional<Equipo> buscarPorSerie(String serie);
    List<Equipo> listarPorCliente(Long idCliente);
    List<Equipo> listarPorEmpresa(Long empresaId);
    List<Equipo> listarPorEstado(String estado);
    List<Equipo> listarPorUbicacion(String ubicacion);
    void actualizarEstadoInterno(Integer id, String estadoInterno);
    void actualizarEstadoInternoPorCliente(Long idCliente, String estadoInterno);
    void actualizarEstadoInternoPorEmpresa(Long empresaId, String estadoInterno);
    void eliminar(Integer id);
}

