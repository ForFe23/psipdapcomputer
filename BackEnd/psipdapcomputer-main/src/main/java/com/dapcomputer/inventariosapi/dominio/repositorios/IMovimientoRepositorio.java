package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.MovimientoEquipo;
import java.util.List;

public interface IMovimientoRepositorio {
    MovimientoEquipo registrar(MovimientoEquipo movimiento);
    List<MovimientoEquipo> listarPorEquipo(Integer equipoId);
}
