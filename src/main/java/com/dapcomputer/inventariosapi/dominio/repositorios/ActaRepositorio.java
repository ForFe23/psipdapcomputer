package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import java.util.List;
import java.util.Optional;

public interface ActaRepositorio {
    Acta guardar(Acta acta);
    Optional<Acta> buscarPorId(Integer id);
    List<Acta> listar();
}
