package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import java.util.List;

public interface IncidenteRepositorio {
    Incidente guardar(Incidente incidente);
    List<Incidente> listar();
    List<Incidente> listarPorCliente(Long idCliente);
    List<Incidente> listarPorEquipo(Integer equipoId);
    List<Incidente> listarPorUsuario(Integer idUsuario);
    java.util.Optional<Incidente> buscarPorId(Integer id);
    void actualizarEstadoInternoPorEquipo(Integer equipoId, String estadoInterno);
    void eliminar(Integer id);
}
