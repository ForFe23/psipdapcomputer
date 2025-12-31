package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import java.util.List;

public interface MovimientoRepositorio {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> listarPorEquipo(String serieEquipo);
    List<Movimiento> listarPorUsuario(Integer idUsuario);
}
