package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import java.util.List;

public interface MovimientoRepositorio {
    Movimiento guardar(Movimiento movimiento);
    java.util.Optional<Movimiento> buscarPorId(Integer id);
    java.util.Optional<Movimiento> buscarPorActa(Integer idActa);
    List<Movimiento> listar();
    List<Movimiento> listarPorEquipo(Integer equipoId);
    List<Movimiento> listarPorUsuario(Integer idUsuario);
    void actualizarEstadoInterno(Integer id, String estadoInterno);
    void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno);
    void actualizarEstadoInternoPorCliente(Long idCliente, String estadoInterno);
    void actualizarEstadoInternoPorActa(Integer idActa, String estadoInterno);
}
