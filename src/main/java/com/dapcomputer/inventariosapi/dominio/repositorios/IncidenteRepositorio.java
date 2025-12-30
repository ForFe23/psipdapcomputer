package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import java.util.List;

public interface IncidenteRepositorio {
    Incidente guardar(Incidente incidente);
    List<Incidente> listarPorCliente(Long idCliente);
}
