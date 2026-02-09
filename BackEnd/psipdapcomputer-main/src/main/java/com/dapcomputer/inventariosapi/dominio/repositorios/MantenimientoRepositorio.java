package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import java.util.List;
import java.util.Optional;

public interface MantenimientoRepositorio {
    Mantenimiento guardar(Mantenimiento mantenimiento);
    List<Mantenimiento> listar();
    Optional<Mantenimiento> buscarPorId(Integer id);
    List<Mantenimiento> listarPorEquipoId(Long equipoId);
    void actualizarEstadoPorEquipo(Long equipoId, String estado);
    void actualizarEstadoPorCliente(Long idCliente, String estado);
    void eliminar(Integer id);
}

