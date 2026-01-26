package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import java.util.List;
import java.util.Optional;

public interface IUbicacionRepositorio {
    Ubicacion guardar(Ubicacion ubicacion);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> buscarPorId(Long id);
    List<Ubicacion> listarPorEmpresa(Long empresaId);
}
