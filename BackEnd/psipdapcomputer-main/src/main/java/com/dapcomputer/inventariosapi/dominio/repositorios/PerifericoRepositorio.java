package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import java.util.List;
import java.util.Optional;

public interface PerifericoRepositorio {
    Periferico guardar(Periferico periferico);
    Optional<Periferico> buscarPorId(Integer id);
    List<Periferico> listarPorEquipo(Integer equipoId);
    List<Periferico> listarPorSerie(String serieEquipo);
    List<Periferico> listarPorCliente(Integer idCliente);
    List<Periferico> listarTodos();
    void actualizarEstadoInternoPorEquipo(Integer equipoId, String estadoInterno);
    void eliminar(Integer id);
}
