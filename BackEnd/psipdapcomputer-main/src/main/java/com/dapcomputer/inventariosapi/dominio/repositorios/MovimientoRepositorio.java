package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import java.util.List;

public interface MovimientoRepositorio {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> listar();
    List<Movimiento> listarPorEquipo(Integer equipoId);
    List<Movimiento> listarPorUsuario(Integer idUsuario);
    void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno);
}
