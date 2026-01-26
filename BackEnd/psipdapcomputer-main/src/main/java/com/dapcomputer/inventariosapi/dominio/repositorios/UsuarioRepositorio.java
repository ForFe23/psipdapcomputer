package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositorio {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Integer id);
    List<Usuario> listarPorCliente(Long idCliente);
    List<Usuario> listarTodos();
    void actualizarEstadoInterno(Integer id, String estadoInterno, String estatus);
    void actualizarEstadoInternoPorCliente(Long idCliente, String estadoInterno, String estatus);
    void eliminar(Integer id);
}
