package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.Acta;
import java.util.List;
import java.util.Optional;

public interface IActaRepositorio {
    Acta guardar(Acta acta);
    Acta actualizar(Acta acta);
    Optional<Acta> buscarPorId(Integer id);
    List<Acta> listarPorFiltros(Long empresaId, Long ubicacionId, String estado);
}

