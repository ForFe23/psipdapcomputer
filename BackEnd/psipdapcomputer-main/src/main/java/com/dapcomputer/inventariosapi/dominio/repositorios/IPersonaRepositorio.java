package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.Persona;
import java.util.List;
import java.util.Optional;

public interface IPersonaRepositorio {
    Persona guardar(Persona persona);
    Persona actualizar(Persona persona);
    Optional<Persona> buscarPorId(Integer id);
    List<Persona> listarPorEmpresa(Long empresaId);
}
