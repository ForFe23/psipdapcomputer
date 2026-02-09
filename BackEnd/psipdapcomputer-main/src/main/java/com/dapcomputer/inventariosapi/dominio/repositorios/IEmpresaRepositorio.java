package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.Empresa;
import java.util.List;
import java.util.Optional;

public interface IEmpresaRepositorio {
    Empresa guardar(Empresa empresa);
    Empresa actualizar(Empresa empresa);
    Optional<Empresa> buscarPorId(Long id);
    List<Empresa> listar();
    List<Empresa> listarPorCliente(Long clienteId);
    void actualizarEstadoInterno(Long id, String estadoInterno);
    void actualizarEstadoInternoPorCliente(Long clienteId, String estadoInterno);
    void eliminar(Long id);
}

