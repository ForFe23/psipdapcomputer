package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import java.util.List;

public interface KardexRepositorio {
    void registrar(KardexEntrada entrada);
    List<KardexEntrada> listarRecientes(int limite);
}
