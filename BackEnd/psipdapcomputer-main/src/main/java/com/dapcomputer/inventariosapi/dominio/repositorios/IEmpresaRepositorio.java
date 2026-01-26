package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.Empresa;
import java.util.List;
import java.util.Optional;

public interface IEmpresaRepositorio {
    Empresa guardar(Empresa empresa);
    Empresa actualizar(Empresa empresa);
    Optional<Empresa> buscarPorId(Long id);
    List<Empresa> listar();
}
