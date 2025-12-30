package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import java.util.List;

public interface PerifericoRepositorio {
    Periferico guardar(Periferico periferico);
    List<Periferico> listarPorEquipo(EquipoId id);
}
