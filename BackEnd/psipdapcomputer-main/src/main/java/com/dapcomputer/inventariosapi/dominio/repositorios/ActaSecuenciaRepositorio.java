package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaSecuencia;
import java.util.Optional;

public interface ActaSecuenciaRepositorio {
    ActaSecuencia guardar(ActaSecuencia secuencia);
    Optional<ActaSecuencia> buscarPorClienteYAnio(Integer idCliente, Integer anio);
}

