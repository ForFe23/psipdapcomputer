package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Rol;
import java.util.List;
import java.util.Optional;

public interface RolRepositorio {
    Rol guardar(Rol rol);
    Optional<Rol> buscarPorId(Long id);
    Optional<Rol> buscarPorCodigo(String codigo);
    List<Rol> listar();
    void eliminar(Long id);
}
