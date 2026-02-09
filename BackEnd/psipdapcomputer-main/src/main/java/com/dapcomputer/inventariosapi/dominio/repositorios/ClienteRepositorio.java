package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepositorio {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> listar();
    void actualizarEstadoInterno(Long id, String estadoInterno);
    void eliminar(Long id);
}

