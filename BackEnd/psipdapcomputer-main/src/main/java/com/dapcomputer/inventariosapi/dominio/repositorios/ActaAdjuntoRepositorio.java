package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import java.util.List;
import java.util.Optional;

public interface ActaAdjuntoRepositorio {
    ActaAdjunto guardar(ActaAdjunto adjunto);
    List<ActaAdjunto> listarPorActa(Integer idActa);
    Optional<ActaAdjunto> buscarPorId(Integer id);
    void actualizarEstadoInterno(Integer id, String estadoInterno);
    void actualizarEstadoInternoPorActa(Integer idActa, String estadoInterno);
    void actualizarEstadoInternoPorCliente(Integer idCliente, String estadoInterno);
    void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno);
}
